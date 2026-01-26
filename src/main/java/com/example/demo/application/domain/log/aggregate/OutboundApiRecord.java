package com.example.demo.application.domain.log.aggregate;

import com.example.demo.application.domain.log.aggregate.vo.OutboundApiStatus;
import com.example.demo.application.domain.log.command.RecordFailedOutboundApiCommand;
import com.example.demo.application.domain.log.command.RecordSuccessOutboundApiCommand;
import com.example.demo.application.domain.log.outbound.RecordOutboundApiRequestCommand;
import com.example.demo.infra.outbound.resolver.OutboundApiRequestResolver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 外部 API 呼叫紀錄實體
 *
 * <p>
 * 用於儲存與追蹤所有對外部系統的 API 呼叫資訊， 包含 Request、Response、錯誤訊息與狀態。
 * </p>
 *
 * <h3>設計定位</h3>
 * <ul>
 * <li>Domain / Service 層可用來紀錄 API 呼叫歷程</li>
 * <li>與 {@link RecordOutboundApiRequestCommand} 協作，支援 Outbound API
 * Request/Response Handler</li>
 * <li>狀態分 SAVED / SUCCESS / FAILED，用於追蹤 API 執行結果</li>
 * </ul>
 */
@Entity
@Table(name = "outbound_api_record")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OutboundApiRecord {

	/**
	 * 自動生成的唯一主鍵
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 外部系統代碼 / 名稱 例如 ERP / CRM / PAYMENT
	 */
	@Column(name = "system")
	private String system;

	/**
	 * HTTP 方法類型 例如 GET / POST / PUT / DELETE
	 */
	@Column(name = "http_method")
	private String httpMethod;

	/**
	 * 對應的 Java 方法名稱
	 */
	@Column(name = "method")
	private String method;

	/**
	 * API 路徑 例如 /api/v1/submitForm
	 */
	@Column(name = "url")
	private String apiPath;

	/**
	 * 請求內容（Request 物件 JSON）
	 * <p>
	 * 建議使用 {@link OutboundApiRequestResolver} 解析後序列化
	 * </p>
	 */
	@Column(name = "request_body", columnDefinition = "nvarchar(max)")
	private String requestBody;

	/**
	 * 回應內容（Response 物件 JSON）
	 */
	@Column(name = "response_body", columnDefinition = "nvarchar(max)")
	private String responseBody;

	/**
	 * 錯誤訊息
	 * <p>
	 * 發生例外或 API 呼叫失敗時填寫
	 * </p>
	 */
	@Column(name = "error_message", columnDefinition = "nvarchar(max)")
	private String errorMessage;

	/**
	 * 呼叫狀態
	 * <ul>
	 * <li>SAVED: 初始建立，尚未呼叫</li>
	 * <li>SUCCESS: 成功</li>
	 * <li>FAILED: 失敗</li>
	 * </ul>
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private OutboundApiStatus status;

	// ------------------- Methods -------------------

	/**
	 * 根據 Request Command 建立初始紀錄
	 *
	 * <p>
	 * 此方法用於在 API 呼叫前建立 Outbound API 紀錄，初始化 Request 與狀態為 {@code SAVED} 或
	 * {@code PENDING}
	 * </p>
	 *
	 * @param command 包含 Request 資訊的 Command
	 */
	public void create(RecordOutboundApiRequestCommand command) {
		this.system = command.getSystem();
		this.method = command.getMethod();
		this.httpMethod = command.getHttpMethod();
		this.apiPath = command.getApiPath();
		this.requestBody = command.getRequestBody();
		this.status = OutboundApiStatus.PENDING;
	}

	/**
	 * 標註外部 API 呼叫失敗。
	 *
	 * <p>
	 * 將紀錄狀態轉為 FAILED，並補齊錯誤與回應資訊。
	 * </p>
	 *
	 * @param command {@link RecordFailedOutboundApiCommand}
	 */
	public void markFailed(RecordFailedOutboundApiCommand command) {
		this.status = OutboundApiStatus.FAILED;
		this.responseBody = command.getResponseBody();
		this.errorMessage = command.getErrorMessage();
		this.apiPath = command.getApiPath();
		this.httpMethod = command.getHttpMethod();
	}

	/**
	 * 標註外部 API 呼叫成功。
	 *
	 * <p>
	 * 將紀錄狀態轉為 SUCCESS，並補齊回應資訊。
	 * </p>
	 *
	 * @param command {@link RecordSuccessOutboundApiCommand}
	 */
	public void markSuccess(RecordSuccessOutboundApiCommand command) {
		this.status = OutboundApiStatus.SUCCESS;
		this.responseBody = command.getResponseBody();
		this.apiPath = command.getApiPath();
		this.httpMethod = command.getHttpMethod();
	}

}
