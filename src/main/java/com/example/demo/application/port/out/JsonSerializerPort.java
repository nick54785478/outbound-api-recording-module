package com.example.demo.application.port.out;

/**
 * JSON 序列化 Outbound Port
 *
 * <p>
 * 定義 Application 層所需之物件序列化契約，隔離底層 Jackson 等基礎設施實作技術。
 * 符合命名規範 3-8: out: ... + Port
 * </p>
 */
public interface JsonSerializerPort {

	/**
	 * 將物件序列化為 JSON 字串
	 *
	 * @param target 欲序列化之物件
	 * @return 序列化後之 JSON 字串
	 */
	String serialize(Object target);
}
