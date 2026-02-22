# Outbound API Recording & Validation Module

## 專案定位

本模組旨在**統一管理對外部系統 API 的呼叫紀錄與回應檢核流程**，透過標準化與策略化的設計，
解決過去外部介接中常見的問題，例如：

* API 呼叫紀錄分散在各處，難以追蹤 
* 成功 / 失敗判斷邏輯不一致
* HTTP / Feign 細節滲透進 Domain 與業務邏輯

本模組以 AOP + Hexagonal Architecture + EDA（Event-Driven Architecture） 為核心設計理念，
在不侵入既有 Client 實作的前提下，提供一致、可擴充、可觀測的 Outbound API 管理能力。



**核心目標 （Goals）**

* 完整紀錄：追蹤每一次 API 呼叫的 Request、Response 及 Exception

* 策略化處理：依據不同系統 (system) 與「API 類型」動態選擇對應的 Handler 與 Validator

* 成功/失敗動態判定：支援針對不同 API 定義各自的成功與失敗標準（不再只依賴 HTTP Status）

* 技術解耦：封裝 HTTP / Feign / Exception 細節，避免基礎設施 (Infra Layer) 邏輯污染 Domain 與 Application Layer

---

## 核心架構

本模組利用 **AOP (Aspect-Oriented Programming)** 攔截標註了特定註解的 Client 實作類，
在 API 呼叫前後注入紀錄、驗證與事件發佈行為。


**關鍵元件說明:**


**1. Annotation: @ExternalApiClient**

* **身分標別：** 用於標記某個實作類為外部系統 Client。

* **策略依據：** AOP 與 Factory 會根據註解內的 system 屬性選擇對應策略。

**2. OutboundApiRequestHandlerPort**
  
  * **職責：** 
     * 解析 AOP 攔截到的方法資訊。
     * 提取 Request Body / Path / Params 。
     * 組裝成 RecordOutboundApiRequestCommand 。

  * **設計重點：**
     * 依「外部系統」策略化實作。
     * Request 解析邏輯與 AOP 解耦。

**3. OutboundApiResponseHandlerPort**

  * **職責：** 
  
     * 統一收斂成功 / 失敗後的處理邏輯。
     * 更新 Outbound API 呼叫紀錄狀態。

  * **特性:**
     * 不感知 HTTP / Feign / Exception 細節。
     * 僅處理「結果資料」。


**4. OutboundApiResponseValidatorPort & Strategy**

  * **職責：** 
     * 對外部系統回應內容進行檢核。
     * 驗證失敗時，回傳或拋出對應結果。
  * **雙層結構:**
     * ValidatorPort：驗證入口（依 system 分流）。
     * ApiResponseValidationStrategy：各 API 自定義驗證規則。
     * 註. 此設計可避免 Validator 內部充斥 if-else，並可依 API 精細定義成功條件。


**5. Application Service（OutboundApiRecordApplicationService）**
  * **角色定位：**
     * 統籌 Outbound API 呼叫前後的應用層流程。
  * **責任：**
     * 呼叫 RequestHandler 建立初始紀錄。
     * 驗證 Response。
     * 發佈成功 / 失敗 Domain Event 。
  * **注意**
     * 不包含 AOP 與 Event Listener 技術細節。

**6. Event-Driven Architecture（EDA**

  * **成功 / 失敗結果：**
     * 轉換為 Domain Event 發佈。
  * **轉換為 Domain Event 發佈**
     * 由 OutboundApiEventHandler 非同步接收。
     * 再委派給對應的 ResponseHandler 。
  


--- 

## 監控執行流程

當標註有 @ExternalApiClient 的方法被呼叫時，會依序執行以下步驟：


**1. 解析階段：** 

>* 解析 Request 。
>* 建立並儲存初始 OutboundApiRecord 。

**2. 執行階段：** 

>* 執行原有的 API 呼叫方法（Proceed）。

**3. 驗證階段：** 

>* 呼叫 Validator 與 Strategy 進行回應檢核 。
>* 完成階段（EDA）

**4. 完成階段（EDA）：**

>* 成功：更新狀態為 SUCCESS 。
>* 失敗：更新狀態為 FAILED 。


## 技術筆記（Technical Notes）

* **ThreadLocal Context：**

>* 使用 ContextHolder 保存 OutboundApiRequestInfo。
>* 確保多執行緒 / 非同步環境下資料隔離。

* **Feign 整合建議：**

>* 使用 RequestInterceptor 注入 Token / Header。
>* 使用 ErrorDecoder + Exception Mapper 處理底層錯誤。

* **驗證時機：**

>* Response Validator 必須在 proceed() 之後執行。
>* 才能取得實際回傳物件。


---

## 使用規範與注意事項

* **標註位置：** @ExternalApiClient 必須標註在 Client 實作類而非 Interface 上 。

* **系統唯一性：** 定義的 system 名稱必須唯一，以確保正確對應 Handler 與 Validator 。

* **驗證順序：** Response Validator 必須放在 AOP 的 proceed() 之後執行，才能解析到實際的回傳物件 。

* **例外處理：** 避免在 Feign Client 內部直接拋出 Exception，應交由 AOP 或 Validator 層統一攔截處理 。

* **Feign 整合：** 建議配合 RequestInterceptor 處理 Token/Header 注入，並利用 ErrorDecoder 配合 Exception Mapper 處理底層錯誤 。

---

## 流程圖:
graph TD
    
	Start([Application Service]) --> Call[Feign Client 實作類]
	
	subgraph AOP_Intercept [AOP 統一攔截: RecordOutboundApiAspect]
	    Call -.->|標註 @ExternalApiClient| Aspect{AOP 攔截}
	
	    %% Request 階段
	    Aspect --> ReqFactory[OutboundApiRequestHandlerFactory]
	    ReqFactory --> ParseReq[解析方法參數 / 提取 Path 與 Body]
	    ParseReq --> Cmd[組裝 RecordOutboundApiRequestCommand]
	    Cmd --> Repo[OutboundApiRecordRepository<br/>存入初始紀錄]
	
	    %% 執行階段
	    Repo --> Proceed[執行原方法 proceed]
	
	    %% Response / 驗證階段
	    Proceed --> Validator[OutboundApiResponseValidatorPort]
	    Validator --> Strategy[ApiResponseValidationStrategy<br/>各 API 自定義檢核]
	
	    Strategy --> Judge{驗證結果}
	end
	
	Judge -->|成功| SuccessEvent[發佈成功事件]
	SuccessEvent --> UpdateSuccess[更新狀態為 SUCCESS]
	
	Judge -->|失敗| FailEvent[發佈失敗事件]
	FailEvent --> UpdateFail[更新狀態為 FAILED]
	UpdateFail --> Throw[拋出 Exception]
	
	UpdateSuccess --> End([回傳結果])
