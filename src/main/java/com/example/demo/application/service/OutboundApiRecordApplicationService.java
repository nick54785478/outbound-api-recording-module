package com.example.demo.application.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import com.example.demo.application.domain.log.aggregate.OutboundApiRecord;
import com.example.demo.application.domain.log.command.OutboundApiFailedCommand;
import com.example.demo.application.domain.log.command.OutboundApiSucceededCommand;
import com.example.demo.application.domain.log.outbound.RecordOutboundApiRequestCommand;
import com.example.demo.application.factory.OutboundApiRequestHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.port.OutboundApiRequestHandlerPort;
import com.example.demo.application.port.OutboundApiResponseHandlerPort;
import com.example.demo.application.port.OutboundApiResponseValidatorPort;
import com.example.demo.config.context.ContextHolder;
import com.example.demo.config.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.persistence.OutboundApiRecordRepository;
import com.example.demo.util.JsonParseUtil;

import lombok.AllArgsConstructor;

/**
 * Outbound API 呼叫紀錄應用服務
 *
 * <p>
 * 此 Service 封裝了整個 Outbound API 呼叫流程的業務邏輯：
 * <ul>
 * <li>請求前處理：建立 API 呼叫紀錄</li>
 * <li>回應後處理：驗證 Response、成功紀錄處理</li>
 * <li>例外處理：統一處理失敗狀態</li>
 * </ul>
 * </p>
 *
 * <p>
 * AOP 切面可以直接呼叫本 Service 以簡化攔截邏輯，並提高測試與維護性。
 * </p>
 */
@Service
@AllArgsConstructor
public class OutboundApiRecordApplicationService {

	/**
	 * Request Handler 工廠，用於取得 RequestHandler ，來解析 API 呼叫參數
	 */
	private final OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory;

	/**
	 * Outbound API 紀錄 Repository
	 */
	private final OutboundApiRecordRepository outboundApiRecordRepository;

	/**
	 * Response Validator 工廠，用於取得 Response Validator ，來解析回應驗證
	 */
	private final OutboundApiResponseValidatorFactory validatorFactory;

	/**
	 * Response Handler 工廠，，用於取得 Response Handler ，來處理成功/失敗回應
	 */
	private final OutboundApiResponseHandlerFactory outboundApiResponseHandlerFactory;

	/**
	 * 外部 API 呼叫前處理
	 *
	 * <p>
	 * 將方法參數解析成 Record Command，建立 OutboundApiRecord 並儲存。 通常在 AOP 的原方法執行前呼叫。
	 * </p>
	 *
	 * @param system    外部系統代碼，對應
	 *                  {@link OutboundApiRequestHandlerPort#supportSystem()}
	 * @param joinPoint AOP 切入點，包含方法參數與目標方法
	 * @return 儲存後的 {@link OutboundApiRecord} 實體
	 */
	public OutboundApiRecord preExecutingOutboundApi(String system, ProceedingJoinPoint joinPoint) {
		// 取得 Request Handler
		OutboundApiRequestHandlerPort requestHandler = outboundApiRequestHandlerFactory.getHandler(system);

		// 將方法參數轉換為 Request Command
		RecordOutboundApiRequestCommand command = requestHandler.resolveRequest(joinPoint);

		// 建立 OutboundApiRecord 並儲存
		OutboundApiRecord outboundApiRecord = new OutboundApiRecord();
		outboundApiRecord.create(command);
		return outboundApiRecordRepository.save(outboundApiRecord);
	}

	/**
	 * 外部 API 呼叫後處理
	 *
	 * <p>
	 * 包含回應驗證與成功處理。 - 驗證 Response 是否符合規範，必要時拋出例外 - 成功呼叫則更新 Outbound API 紀錄
	 * </p>
	 *
	 * @param system  外部系統代碼
	 * @param proceed 原方法執行後回傳的 Response 物件
	 * @param saved   對應的 OutboundApiRecord
	 */
	public void afterExecutingOutboundApi(String system, Object proceed, OutboundApiRecord saved) {
		OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

		// 呼叫 Validator，直接傳入物件，不需序列化
		OutboundApiResponseValidatorPort validator = validatorFactory.get(system);
		validator.validate(proceed, feignContext);

		// 成功處理：更新紀錄
		OutboundApiSucceededCommand outboundApiSucceededCommand = OutboundApiSucceededCommand.builder()
				.savedId(saved.getId()).apiPath(feignContext.getUrl()).httpMethod(feignContext.getHttpMethod())
				.responseBody(JsonParseUtil.serialize(proceed)).build();

		// 取得 Response Handler
		OutboundApiResponseHandlerPort responseHandler = outboundApiResponseHandlerFactory.getHandler(system);
		responseHandler.handleSuccess(outboundApiSucceededCommand);
	}

	/**
	 * 外部 API 呼叫例外處理
	 *
	 * <p>
	 * 將例外訊息轉換成 OutboundApiFailedCommand 並交給 Response Handler 處理。 通常在 AOP 的 catch
	 * 區塊呼叫。
	 * </p>
	 *
	 * @param system           外部系統代碼
	 * @param saved            對應的 OutboundApiRecord
	 * @param exceptionMessage 發生的例外訊息
	 */
	public void handleException(String system, OutboundApiRecord saved, String exceptionMessage) {
		OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

		OutboundApiFailedCommand outboundApiFailedCommand = OutboundApiFailedCommand.builder().savedId(saved.getId())
				.apiPath(feignContext.getUrl()).httpMethod(feignContext.getHttpMethod()).errorMessage(exceptionMessage)
				.build();

		OutboundApiResponseHandlerPort responseHandler = outboundApiResponseHandlerFactory.getHandler(system);
		responseHandler.handleFailure(outboundApiFailedCommand);
	}
}
