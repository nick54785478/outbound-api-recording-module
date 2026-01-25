package com.example.demo.application.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.OutboundApiResponseHandlerPort;

/**
 * Outbound API Response Handler Factory
 *
 * <p>
 * 此 Factory 負責依「外部系統代碼（system）」選擇 對應的 {@link OutboundApiResponseHandler} 實作。
 * </p>
 *
 * <h3>設計說明</h3>
 * <ul>
 * <li>透過 Spring 注入所有 {@link OutboundApiResponseHandler} 實作</li>
 * <li>以 {@link OutboundApiResponseHandler#supportSystem()} 作為唯一鍵</li>
 * <li>在執行期動態選擇正確的 Response Handler</li>
 * </ul>
 *
 * <h3>使用時機</h3>
 * <ul>
 * <li>外部 API 呼叫完成（成功或失敗）後</li>
 * <li>需根據不同外部系統，採用不同的回應處理策略</li>
 * </ul>
 *
 * <h3>錯誤處理</h3>
 * <ul>
 * <li>若找不到對應 system 的 Handler，將直接拋出 {@link IllegalStateException}</li>
 * <li>代表系統設定或實作不完整，屬於「不可恢復」的系統錯誤</li>
 * </ul>
 */
@Component
public class OutboundApiResponseHandlerFactory {

	/**
	 * system → OutboundApiResponseHandler 的不可變映射表
	 */
	private final Map<String, OutboundApiResponseHandlerPort> handlerMap;

	/**
	 * 建立 Response Handler Registry。
	 *
	 * <p>
	 * Spring 會自動注入所有 {@link OutboundApiResponseHandler} 的實作， 並依其
	 * {@link OutboundApiResponseHandler#supportSystem()} 建立索引。
	 * </p>
	 *
	 * @param handlers 所有已註冊的 Response Handler
	 */
	public OutboundApiResponseHandlerFactory(List<OutboundApiResponseHandlerPort> handlers) {

		this.handlerMap = handlers.stream().collect(
				Collectors.toUnmodifiableMap(OutboundApiResponseHandlerPort::supportSystem, Function.identity()));
	}

	/**
	 * 依外部系統代碼取得對應的 {@link OutboundApiResponseHandler}。
	 *
	 * @param system 外部系統代碼（如 ERP / CRM / PAYMENT）
	 * @return 對應的 Response Handler
	 * @throws IllegalStateException 若查無對應 Handler
	 */
	public OutboundApiResponseHandlerPort getHandler(String system) {
		OutboundApiResponseHandlerPort handler = handlerMap.get(system);
		if (handler == null) {
			throw new IllegalStateException("No ExternalApiResponseHandler for system: " + system);
		}
		return handler;
	}
}
