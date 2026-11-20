package com.example.demo.application.shared.outbound.context;

/**
 * 外部 API 呼叫請求資訊封裝物件。
 *
 * <p>
 * 用於描述單次對外系統（如 ERP、AuthService）呼叫的基本請求資訊，
 * 通常搭配 ThreadLocal 保存於請求生命週期中。
 * </p>
 *
 * @param system 系統名稱
 * @param httpMethod HTTP Method（GET / POST / PUT / PATCH / DELETE）
 * @param url 呼叫的 API URL（通常為 Feign 組合後的相對路徑）
 * @param api API 資源路徑
 */
public record OutboundApiRequestInfo(
		String system,
		String httpMethod,
		String url,
		String api
) {

	public OutboundApiRequestInfo() {
		this(null, null, null, null);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String system;
		private String httpMethod;
		private String url;
		private String api;

		public Builder system(String system) {
			this.system = system;
			return this;
		}

		public Builder httpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder api(String api) {
			this.api = api;
			return this;
		}

		public OutboundApiRequestInfo build() {
			return new OutboundApiRequestInfo(system, httpMethod, url, api);
		}
	}
}
