package com.example.demo.infra.context;

import com.example.demo.infra.context.element.OutboundApiRequestInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Feign 呼叫期間的上下文容器。
 *
 * <p>
 * 使用 ThreadLocal 保存單次請求生命週期內的 {@link OutboundApiRequestInfo}，確保：
 * <ul>
 * <li>同一個 HTTP Request 中的 Feign 呼叫可共用資訊</li>
 * <li>避免多執行緒之間的資料污染</li>
 * </ul>
 *
 * <p>
 * 常見使用場景：
 * <ul>
 * <li>Outbound API 呼叫紀錄</li>
 * <li>Exception 發生時補充外部呼叫資訊</li>
 * <li>AOP 行為紀錄 / Trace 記錄</li>
 * </ul>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolder {

	/**
	 * 儲存 Feign 呼叫資訊的 ThreadLocal
	 */
	private static final ThreadLocal<OutboundApiRequestInfo> FEIGN_CONTEXT = new ThreadLocal<>();

	/**
	 * 設定當前執行緒的 Feign 呼叫上下文
	 *
	 * @param info 外部 API 請求資訊
	 */
	public static void setFeignContext(OutboundApiRequestInfo info) {
		FEIGN_CONTEXT.set(info);
	}

	/**
	 * 取得當前執行緒的 Feign 呼叫上下文
	 *
	 * @return 外部 API 請求資訊，若不存在則回傳 null
	 */
	public static OutboundApiRequestInfo getFeignContext() {
		return FEIGN_CONTEXT.get();
	}

	/**
	 * 清除當前執行緒的 Feign 呼叫上下文。
	 *
	 * <p>
	 * 建議於請求結束或 finally 區塊中呼叫， 避免 ThreadLocal 記憶體洩漏。
	 */
	public static void clear() {
		FEIGN_CONTEXT.remove();
	}
}
