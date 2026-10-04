package com.example.demo.application.shared.outbound.context;

/**
 * 外部 API 呼叫請求資訊封裝物件。
 *
 * <p>
 * 用於描述單次對外系統（如 ERP）呼叫的基本請求資訊， 通常搭配 ThreadLocal 保存於請求生命週期中。
 */
public class OutboundApiRequestInfo {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * HTTP Method（GET / POST / PUT / PATCH / DELETE）
	 */
	private String httpMethod;

	/**
	 * 呼叫的 API URL（通常為 Feign 組合後的相對路徑）
	 */
	private String url;

	/**
	 * API 資源路徑
	 */
	private String api;

	public OutboundApiRequestInfo() {
	}

	public OutboundApiRequestInfo(String system, String httpMethod, String url, String api) {
		this.system = system;
		this.httpMethod = httpMethod;
		this.url = url;
		this.api = api;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
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
