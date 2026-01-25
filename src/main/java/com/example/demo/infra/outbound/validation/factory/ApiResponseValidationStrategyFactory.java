package com.example.demo.infra.outbound.validation.factory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.infra.outbound.validation.strategy.base.ApiResponseValidationStrategy;

/**
 * API 回應驗證策略工廠。
 *
 * <p>
 * 負責依據「外部系統 + API 路徑」在執行期動態選擇 對應的 {@link ApiResponseValidationStrategy}。
 * </p>
 *
 * <p>
 * 策略 Key 組成規則：
 * 
 * <pre>
 * system:api
 * </pre>
 *
 * <p>
 * 範例：
 * <ul>
 * <li>{@code AuthService:/api/v1/login}</li>
 * <li>{@code AuthService:/api/v1/auth/permissions}</li>
 * </ul>
 * </p>
 *
 * <p>
 * 設計說明：
 * <ul>
 * <li>透過 Spring 注入所有 Strategy 實作</li>
 * <li>於建構時即轉換為 Map，提高查找效率</li>
 * <li>避免於執行期進行 if-else / switch 判斷</li>
 * </ul>
 * </p>
 */
@Component
public class ApiResponseValidationStrategyFactory {

	/**
	 * 驗證策略快取表。
	 *
	 * <p>
	 * Key 格式：{@code system:api} Value：對應的驗證策略實作
	 * </p>
	 */
	private final Map<String, ApiResponseValidationStrategy> strategyMap;

	/**
	 * 建立 API 回應驗證策略工廠。
	 *
	 * <p>
	 * 由 Spring 注入所有 {@link ApiResponseValidationStrategy} 實作， 並於初始化時轉換為 Map 以利快速查找。
	 * </p>
	 *
	 * @param strategies 系統中所有已註冊的 API 回應驗證策略
	 */
	public ApiResponseValidationStrategyFactory(List<ApiResponseValidationStrategy> strategies) {

		this.strategyMap = strategies.stream()
				.collect(Collectors.toMap(s -> key(s.system(), s.api()), Function.identity()));
	}

	/**
	 * 依外部系統與 API 路徑取得對應的驗證策略。
	 *
	 * <p>
	 * 若系統中未定義對應策略，將回傳 {@link Optional#empty()}， 由呼叫端決定是否略過驗證或採用預設行為。
	 * </p>
	 *
	 * @param system 外部系統名稱
	 * @param api    API 路徑
	 * @return 對應的 API 回應驗證策略（若存在）
	 */
	public Optional<ApiResponseValidationStrategy> get(String system, String api) {

		return Optional.ofNullable(strategyMap.get(this.key(system, api)));
	}

	/**
	 * 組合驗證策略查找用的 Key。
	 *
	 * <p>
	 * Key 格式：
	 * 
	 * <pre>
	 * system:api
	 * </pre>
	 * </p>
	 *
	 * @param system 外部系統名稱
	 * @param api    API 路徑
	 * @return 組合後的策略 Key
	 */
	private String key(String system, String api) {
		return system + ":" + api;
	}
}
