package com.example.demo.infra.outbound.resolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.example.demo.application.domain.log.outbound.RecordOutboundApiRequestCommand;
import com.example.demo.config.annotation.ExternalApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Outbound API Request Resolver
 *
 * <p>
 * 專責將 AOP 攔截到的 {@link ProceedingJoinPoint}， 解析並轉換為
 * {@link RecordOutboundApiRequestCommand} 中 「Request 相關資訊」的組裝器（Assembler /
 * Resolver）。
 * </p>
 *
 * <h3>設計定位</h3>
 * <ul>
 * <li>只負責「方法層級資訊」與「呼叫輸入資料」的解析</li>
 * <li>不耦合任何 HTTP Client 技術（Feign / RestTemplate / WebClient）</li>
 * <li>可被不同 Adapter 重複使用</li>
 * </ul>
 *
 * <h3>主要解析內容</h3>
 * <ul>
 * <li>系統名稱：透過 {@link ExternalApiClient} 註解取得</li>
 * <li>API 方法名稱：使用 Java Method Name 作為追蹤識別</li>
 * <li>RequestBody：序列化全部方法參數</li>
 * <li>RequestParams：抽取 {@link Map} 類型參數</li>
 * <li>PathVariables：抽取基本型別參數（Fallback 機制）</li>
 * </ul>
 *
 * <h3>責任邊界</h3>
 * <ul>
 * <li>HTTP Method 與 API Path 由呼叫端或 Adapter 提供</li>
 * <li>Response / Error / Status 不在此類處理範圍</li>
 * <li>不解析實際 HTTP 傳輸內容</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class OutboundApiRequestResolver {

	/**
	 * Jackson {@link ObjectMapper}， 用於將方法參數序列化為 JSON 以供紀錄。
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * RequestBody 最大允許長度。
	 * <p>
	 * 避免單筆 API 呼叫紀錄過大， 導致資料庫儲存與查詢效能問題。
	 * </p>
	 */
	private static final int MAX_LOG_LENGTH = 3000;

	/**
	 * 將 AOP 攔截到的 {@link ProceedingJoinPoint} 解析為
	 * {@link RecordOutboundApiRequestCommand}（僅限 Request 資訊）。
	 *
	 * <p>
	 * 本方法僅關心「呼叫前可得資訊」， 回應結果需由其他元件補齊後續狀態。
	 * </p>
	 *
	 * @param joinPoint AOP 切入點，包含方法、目標物件與實際參數
	 * @return 已填充 Request 資訊的 {@link RecordOutboundApiRequestCommand}
	 */
	public RecordOutboundApiRequestCommand resolveRequest(ProceedingJoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Class<?> targetClass = joinPoint.getTarget().getClass();

		// 取得外部系統名稱（若未標註則回傳 UNKNOWN）
		ExternalApiClient clientAnn = targetClass.getAnnotation(ExternalApiClient.class);
		String system = clientAnn != null ? clientAnn.system() : "UNKNOWN";

		// 使用 Java 方法名稱作為 API Method 識別
		String apiMethod = method.getName();

		Object[] args = joinPoint.getArgs();

		// 將全部參數序列化為 RequestBody（供稽核與除錯）
		String requestBody = serializeRequestBody(args);

		// 抽取 Map 型別參數作為 RequestParams
		String requestParams = extractRequestParams(args);

		// 抽取基本型別作為 PathVariables（Fallback）
		String pathVariables = extractPathVariables(args);

		return RecordOutboundApiRequestCommand.builder().system(system).method(apiMethod).requestBody(requestBody)
				.requestParams(requestParams).pathVariables(pathVariables).build();
	}

	// ------------------------------------------------------------------------
	// Helper Methods
	// ------------------------------------------------------------------------

	/**
	 * 將方法參數序列化為 JSON 字串作為 RequestBody 紀錄。
	 *
	 * <p>
	 * 若序列化失敗，則使用 {@link Arrays#toString(Object[])} 作為保底輸出。
	 * </p>
	 *
	 * <p>
	 * 若序列化後字串長度超過 {@link #MAX_LOG_LENGTH}， 則進行截斷以避免資料庫負擔。
	 * </p>
	 *
	 * @param args 方法實際參數
	 * @return JSON 字串、截斷後字串或 fallback 字串
	 */
	private String serializeRequestBody(Object[] args) {
		if (args == null || args.length == 0) {
			return null;
		}
		try {
			String json = OBJECT_MAPPER.writeValueAsString(args);
			if (json.length() > MAX_LOG_LENGTH) {
				return json.substring(0, MAX_LOG_LENGTH) + " ...(truncated)";
			}
			return json;
		} catch (Exception e) {
			return Arrays.toString(args);
		}
	}

	/**
	 * 從方法參數中抽取 {@link Map} 類型作為 RequestParams。
	 *
	 * <p>
	 * 適用於 GET / 查詢型 API， 目前假設 Map 中的 key 可直接轉為字串。
	 * </p>
	 *
	 * @param args 方法實際參數
	 * @return RequestParams JSON 字串，若無則回傳 null
	 */
	private String extractRequestParams(Object[] args) {
		if (args == null || args.length == 0) {
			return null;
		}
		Map<String, Object> params = new LinkedHashMap<>();

		for (Object arg : args) {
			if (arg instanceof Map<?, ?> map) {
				map.forEach((k, v) -> params.put(String.valueOf(k), v));
			}
		}

		if (params.isEmpty()) {
			return null;
		}
		try {
			return OBJECT_MAPPER.writeValueAsString(params);
		} catch (Exception e) {
			return params.toString();
		}
	}

	/**
	 * 從方法參數中抽取基本型別（{@link String} / {@link Number}） 作為 PathVariables 的 fallback 紀錄。
	 *
	 * <p>
	 * 此機制不保證變數名稱正確， 僅作為缺乏明確標註時的補救方案。
	 * </p>
	 *
	 * @param args 方法實際參數
	 * @return PathVariables JSON 字串，若無則回傳 null
	 */
	private String extractPathVariables(Object[] args) {
		if (args == null || args.length == 0) {
			return null;
		}
		Map<String, Object> pathVars = new LinkedHashMap<>();

		for (Object arg : args) {
			if (arg instanceof String || arg instanceof Number) {
				pathVars.put("arg", arg); // fallback
			}
		}

		if (pathVars.isEmpty()) {
			return null;
		}
		try {
			return OBJECT_MAPPER.writeValueAsString(pathVars);
		} catch (Exception e) {
			return pathVars.toString();
		}
	}
}
