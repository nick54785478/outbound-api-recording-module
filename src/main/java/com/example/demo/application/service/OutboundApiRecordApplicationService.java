package com.example.demo.application.service;

import com.example.demo.application.factory.OutboundApiRequestHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.port.EventPublisherPort;
import com.example.demo.application.port.OutboundApiRecordLoggerPort;
import com.example.demo.application.port.OutboundApiRequestHandlerPort;
import com.example.demo.application.port.OutboundApiResponseValidatorPort;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.application.shared.event.RecordOutboundApiFailedEvent;
import com.example.demo.application.shared.event.RecordOutboundApiFailedEvent.RecordOutboundApiFailedEventData;
import com.example.demo.application.shared.event.RecordOutboundApiSucceededEvent;
import com.example.demo.application.shared.event.RecordOutboundApiSucceededEvent.RecordOutboundApiEventData;
import com.example.demo.application.shared.outbound.context.ContextHolder;
import com.example.demo.application.shared.outbound.context.OutboundApiRequestInfo;
import com.example.demo.infra.util.JsonParseUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
public class OutboundApiRecordApplicationService {

    /**
     * Request Handler 工廠，用於取得 RequestHandler ，來解析 API 呼叫參數
     */
    private final OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory;

    private final OutboundApiRecordLoggerPort outboundApiRecordLogger;

    /**
     * Response Validator 工廠，用於取得 Response Validator ，來解析回應驗證
     */
    private final OutboundApiResponseValidatorFactory validatorFactory;

    /**
     * Event Publisher
     */
    private final EventPublisherPort eventPublisher;

    public OutboundApiRecordApplicationService(
            OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory,
            OutboundApiRecordLoggerPort outboundApiRecordLogger,
            OutboundApiResponseValidatorFactory validatorFactory,
            EventPublisherPort eventPublisher) {
        this.outboundApiRequestHandlerFactory = outboundApiRequestHandlerFactory;
        this.outboundApiRecordLogger = outboundApiRecordLogger;
        this.validatorFactory = validatorFactory;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 外部 API 呼叫前處理
     *
     * <p>
     * 將方法參數解析成 Record Command，建立 OutboundApiRecord 並儲存。 通常在 AOP 的原方法執行前呼叫。
     * </p>
     *
     * @param system 外部系統代碼，對應
     * @param target 目標物件
     * @param method 目標方法
     * @param args   方法參數
     * @return 儲存後的紀錄 ID
     */
    public Long preExecutingOutboundApi(String system, Object target, java.lang.reflect.Method method, Object[] args) {
        // 取得 Request Handler
        OutboundApiRequestHandlerPort requestHandler = outboundApiRequestHandlerFactory.getHandler(system);

        // 將方法參數轉換為 Request Command
        RecordOutboundApiRequestCommand command = requestHandler.resolveRequest(target, method, args);

        // 透過 Port 儲存紀錄並回傳 ID
        return outboundApiRecordLogger.save(command);
    }

    /**
     * 外部 API 呼叫後處理（EDA 版本）
     *
     * <p>
     * 此方法負責在外部 API 成功呼叫後：
     * <ul>
     * <li>驗證回應內容是否符合系統規範</li>
     * <li>將成功結果封裝為 Domain Event 並發送</li>
     * </ul>
     * </p>
     *
     * <p>
     * 本方法<strong>不直接處理成功後的資料更新或副作用</strong>， 而是透過 Event Driven Architecture（EDA），
     * 將後續行為交由對應的 Event Listener 負責。
     * </p>
     *
     * <p>
     * 常見的 Listener 責任包含：
     * <ul>
     * <li>更新 OutboundApiRecord 狀態</li>
     * <li>寫入稽核或操作紀錄</li>
     * <li>非同步通知或後續流程觸發</li>
     * </ul>
     * </p>
     *
     * @param system   外部系統代碼
     * @param proceed  原方法執行後回傳的 Response 物件
     * @param recordId 對應的紀錄 ID
     */
    public void afterExecutingOutboundApi(String system, Object proceed, Long recordId) {

        // 取得當前請求的外部 API 呼叫上下文（URL、HTTP Method 等）
        OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

        // 回應驗證（可能拋出例外以中斷主流程）
        // Validator 僅負責規則檢查，不處理任何 side effect
        OutboundApiResponseValidatorPort validator = validatorFactory.get(system);
        validator.validate(proceed, feignContext);

        // 建立「外部 API 成功」事件
        RecordOutboundApiSucceededEvent event = new RecordOutboundApiSucceededEvent(
                UUID.randomUUID().toString(), // eventLogUuid
                UUID.randomUUID().toString(), // targetId
                system,
                new RecordOutboundApiEventData(
                        recordId,
                        feignContext.getUrl(),
                        feignContext.getHttpMethod(),
                        JsonParseUtil.serialize(proceed)
                )
        );

        // 發送 Domain Event，由 Listener 處理後續流程
        eventPublisher.publish(event);
    }

    /**
     * 外部 API 呼叫例外處理（EDA 版本）
     *
     * <p>
     * 當外部 API 呼叫或後處理流程發生例外時， 將錯誤資訊封裝為失敗事件並發送。
     * </p>
     *
     * <p>
     * 本方法不負責例外的最終處理結果（例如 DB 更新或補償行為）， 僅負責將錯誤狀態轉換為 Domain Event， 由對應的 Listener
     * 決定後續行為。
     * </p>
     *
     * <p>
     * 常見的 Listener 行為包含：
     * <ul>
     * <li>標記 OutboundApiRecord 為失敗</li>
     * <li>紀錄錯誤 Log 或告警</li>
     * <li>觸發補償或重試流程</li>
     * </ul>
     * </p>
     *
     * @param system           外部系統代碼
     * @param recordId         對應的紀錄 ID
     * @param exceptionMessage 發生的例外訊息
     */
    public void handleException(String system, Long recordId, String exceptionMessage) {

        // 取得當前請求的外部 API 呼叫上下文
        OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

        // 建立「外部 API 失敗」事件
        RecordOutboundApiFailedEvent event = new RecordOutboundApiFailedEvent(
                UUID.randomUUID().toString(), // eventLogUuid
                UUID.randomUUID().toString(), // targetId
                system,
                new RecordOutboundApiFailedEventData(
                        recordId,
                        feignContext.getUrl(),
                        exceptionMessage,
                        null, // responseBody
                        feignContext.getHttpMethod()
                )
        );

        // 發送失敗事件，由 Listener 負責實際錯誤處理
        eventPublisher.publish(event);
    }

}
