package com.example.demo.application.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.OutboundApiRequestHandlerPort;

/**
 * Outbound API Request Handler Factory
 *
 * <p>
 * 此 Factory 負責依「外部系統代碼（system）」選擇 對應的 {@link OutboundApiRequestHandler} 實作，
 * 用於解析並建立外部 API 呼叫的 Request 紀錄。
 * </p>
 *
 * <h3>設計說明</h3>
 * <ul>
 * <li>採用 Strategy Pattern，依 system 進行行為切換</li>
 * <li>所有 Handler 皆由 Spring 管理並自動註冊</li>
 * <li>Factory 僅負責選擇，不包含任何解析邏輯</li>
 * </ul>
 *
 * <h3>使用時機</h3>
 * <ul>
 * <li>外部 API 實際呼叫前</li>
 * <li>需要依外部系統特性解析 Request 資訊時</li>
 * </ul>
 *
 * <h3>錯誤處理</h3>
 * <ul>
 * <li>若找不到對應 system 的 Handler，視為系統設定錯誤</li>
 * <li>會直接拋出 {@link IllegalStateException}</li>
 * </ul>
 */
@Component
public class OutboundApiRequestHandlerFactory {

	/**
	 * system → OutboundApiRequestHandler 的不可變映射表
	 */
	private final Map<String, OutboundApiRequestHandlerPort> handlerMap;

	/**
	 * 建立 Request Handler Registry。
	 *
	 * <p>
	 * Spring 會注入所有 {@link OutboundApiRequestHandler} 的實作， 並依其
	 * {@link OutboundApiRequestHandler#supportSystem()} 建立索引。
	 * </p>
	 *
	 * @param handlers 所有已註冊的 Request Handler
	 */
	public OutboundApiRequestHandlerFactory(List<OutboundApiRequestHandlerPort> handlers) {

		this.handlerMap = handlers.stream()
				.collect(Collectors.toUnmodifiableMap(OutboundApiRequestHandlerPort::supportSystem, Function.identity()));
	}

	/**
	 * 依外部系統代碼取得對應的 {@link OutboundApiRequestHandler}。
	 *
	 * @param system 外部系統代碼（如 ERP / CRM / PAYMENT）
	 * @return 對應的 Request Handler
	 * @throws IllegalStateException 若查無對應 Handler
	 */
	public OutboundApiRequestHandlerPort getHandler(String system) {
		OutboundApiRequestHandlerPort handler = handlerMap.get(system);
		if (handler == null) {
			throw new IllegalStateException("No OutboundApiRequestHandler for system: " + system);
		}
		return handler;
	}
}
