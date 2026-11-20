package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.out.JsonSerializerPort;
import com.example.demo.infra.util.JsonParseUtil;

/**
 * JSON 序列化 Outbound Adapter
 *
 * <p>
 * 實作 {@link JsonSerializerPort}，委派給基礎設施層的 {@link JsonParseUtil}。
 * 符合命名規範 3-9: out: ... + Adapter
 * </p>
 */
@Component
public class JacksonJsonSerializerAdapter implements JsonSerializerPort {

	@Override
	public String serialize(Object target) {
		return JsonParseUtil.serialize(target);
	}
}
