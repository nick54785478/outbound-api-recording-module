package com.example.demo.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.infra.outbound.resolver.OutboundApiRequestResolver;

/**
 * 標記外部系統 Client 實作的 Annotation
 *
 * <p>
 * 用途：
 * <ul>
 * <li>標註某個 Bean 為外部系統 Client 實作</li>
 * <li>用於 {@link OutboundApiRequestResolver} 或相關 Factory/Handler 選擇對應策略</li>
 * </ul>
 * </p>
 *
 * <p>
 * 使用規範：
 * <ul>
 * <li>必須標註在「Client 實作類」上，而非介面</li>
 * <li>標註的 {@code system} 必須唯一，對應於 Handler Factory 的
 * {@link OutboundApiRequestHandler} / {@link OutboundApiResponseHandler}</li>
 * <li>此 Annotation 僅在 Runtime 可見，用於 AOP 或策略選擇</li>
 * </ul>
 * </p>
 *
 * <p>
 * 範例：
 * 
 * <pre>
 * &#64;Component
 * &#64;ExternalApiClient(system = "ERP")
 * public class ErpClient implements ErpApi {
 *     ...
 * }
 * </pre>
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalApiClient {

	/**
	 * 外部系統代碼
	 *
	 * <p>
	 * 用於識別該 Client 所屬的外部系統，通常對應：
	 * <ul>
	 * <li>Handler Factory 的 key</li>
	 * <li>AOP / Resolver 選擇正確策略</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * 範例：
	 * <ul>
	 * <li>{@code AUTH_PLATFORM}</li>
	 * <li>{@code ERP}</li>
	 * <li>{@code CRM}</li>
	 * </ul>
	 * </p>
	 *
	 * @return 外部系統唯一代碼
	 */
	String system();
}
