# Outbound API Recording & Validation Module

## 專案定位

**本模組旨在**

* 統一管理對外部系統 API 的呼叫紀錄與回應檢核 
* 透過標準化的流程，解決過去外部介接紀錄散亂、判斷邏輯不一的問題 。


**核心目標**

* 完整紀錄：追蹤每一次 API 呼叫的 Request、Response 及 Exception

* 策略化處理：依據不同系統與 API 自動分流至對應的處理器與驗證器

* 成功/失敗動態判定：支援針對不同 API 定義各自的成功與失敗標準

* 技術解耦：將 HTTP 與 Feign 的實作細節封裝，避免基礎建設代碼滲透進 Domain 業務邏輯層

---

## 核心架構

本模組利用 AOP (Aspect-Oriented Programming) 攔截標註了特定註解的 Bean，實現非侵入式的監控與紀錄 。

**關鍵元件說明:**


**1. Annotation: @ExternalApiClient**

* **身分標別：** 用於標記某個實作類為外部系統 Client。

* **策略依據：** AOP 與 Factory 會根據註解內的 system 屬性選擇對應策略。

**2. RequestHandlerPort**
  
* **職責：** 解析 AOP 攔截到的方法參數，提取 Request Body 或 Path 並組裝成 Command 。

**3. ResponseHandlerPort**

* **職責：** 更新 API 呼叫紀錄，收斂成功與失敗的紀錄邏輯，避免 Domain 層處理 HTTP 細節 。

**4. ResponseValidatorPort & Strategy**

* **職責：** 對回應內容進行檢核 。若檢核失敗則拋出 Exception。</li>

* **兩層架構：** 由 ValidatorPort 委派給具體的 ApiResponseValidationStrategy 進行各 API 的自定義檢核。

--- 

## 監控執行流程

當標註有 @ExternalApiClient 的方法被呼叫時，會依序執行以下步驟：


**1. 解析階段：** 解析 Request 並先行存入 OutboundApiRecord 紀錄表中 。

**2. 執行階段：** 執行原有的 API 呼叫方法（Proceed）。

**3. 驗證階段：** 呼叫 Validator 與 Strategy 進行回應檢核 。

**4. 完成階段：**

>* 成功：更新狀態為 SUCCESS 。
>* 失敗：更新狀態為 FAILED 並拋出 Exception 。


技術筆記：模組使用 ThreadLocal (ContextHolder) 保存 Feign 上下文資訊 (OutboundApiRequestInfo)，確保多執行緒環境下的數據隔離。

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
        Cmd --> Repo[OutboundApiRecordRepository <br/> 存入初始紀錄]
        
        %% 執行階段
        Repo --> Proceed[執行原方法 proceed]
        
        %% Response / 驗證階段
        Proceed --> Validator[OutboundApiResponseValidatorPort]
        Validator --> Strategy[ApiResponseValidationStrategy <br/> 各 API 自定義檢核]
        
        Strategy --> Judge{驗證結果}
    end

    %% 結果分支
    Judge -->|成功| Success[OutboundApiResponseHandlerFactory]
    Success --> UpdateSuccess[更新狀態為 SUCCESS]
    
    Judge -->|失敗| Fail[OutboundApiResponseHandlerFactory]
    Fail --> UpdateFail[更新狀態為 FAILED]
    UpdateFail --> Throw[拋出 Exception]

    UpdateSuccess --> End([結束並回傳結果])
