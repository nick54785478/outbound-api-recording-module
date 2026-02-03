package com.example.demo.config.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.infra.context.ContextHolder;
import com.example.demo.infra.context.element.OutboundApiRequestInfo;
import com.example.demo.shared.constant.JwtConstants;

import feign.Client;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.httpclient.ApacheHttpClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AuthFeignConfiguration {

	@Value("${auth.service.endpoint.jwt.token}")
	private String token;

	@Value("${auth.service.endpoint}")
	private String endpoint;

	/**
	 * 定義一個 Feign 的請求攔截器，用於在每次發送請求時增加 Header 資料。
	 *
	 * @return 請求攔截器
	 */
	@Bean
	public RequestInterceptor requestTokenInterceptor() {

		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate requestTemplate) {
				// HTTP Method（GET / POST / PUT / DELETE / PATCH）
				String method = requestTemplate.method();

				// Feign 解析後的請求 URL（通常為相對路徑）
				String url = requestTemplate.url();

				// 建立對外 API 呼叫的請求上下文資訊
				OutboundApiRequestInfo context = OutboundApiRequestInfo.builder().system("AuthService") // 對應系統名稱
						.httpMethod(method).url(String.format("%s%s", endpoint, url)) // 組成完整 url
						.api(url)
						.build();

				log.info("Outbound ERP API Context: {}, Method:{}, Url:{}", context, method, url);

				// 將請求資訊放入 ThreadLocal，供同一請求生命週期使用
				ContextHolder.setFeignContext(context);
				// 在此處新增 JWToken Request Header
				requestTemplate.header(JwtConstants.JWT_HEADER.getValue(), JwtConstants.JWT_PREFIX.getValue() + token);
			}
		};
	}

	/**
	 * 使用 ApacheHttpClient 覆蓋底層預設的 HttpURLConnection 因為 HttpURLConnection 不支援 Patch
	 * 方法。
	 * 
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	@Bean
	public Client feignClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();

		CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		return new ApacheHttpClient(httpClient);
	}

	@Bean
	public Logger.Level authFeignLoggerLevel() {
		return Logger.Level.FULL;
	}
	

}