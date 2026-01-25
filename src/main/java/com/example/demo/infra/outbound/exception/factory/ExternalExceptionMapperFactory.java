package com.example.demo.infra.outbound.exception.factory;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

import com.example.demo.infra.outbound.exception.mapper.ExternalExceptionMapper;

/**
 * 外部系統 Exception Mapper 的工廠類。
 *
 * <p>
 * 負責根據外部系統識別名稱（system）， 從 Spring 容器中選擇對應的 {@link ExternalExceptionMapper}。
 * </p>
 *
 * <p>
 * 設計說明：
 * <ul>
 * <li>每個外部系統可實作一個專屬的 Mapper</li>
 * <li>透過 {@link ExternalExceptionMapper#supports(String)} 判斷是否適用</li>
 * <li>實際選用順序由 Spring 注入的 List 順序決定</li>
 * </ul>
 * </p>
 *
 * <p>
 * ⚠ 前提假設：
 * <ul>
 * <li>系統中<strong>一定存在一個 fallback Mapper</strong>（例如:
 * DefaultExternalExceptionMapper）</li>
 * <li>因此 {@link #get(String)} 理論上不會發生找不到 Mapper 的情況</li>
 * </ul>
 * </p>
 */
@Component
public class ExternalExceptionMapperFactory {

	private final List<ExternalExceptionMapper> mappers;

	public ExternalExceptionMapperFactory(List<ExternalExceptionMapper> mappers) {
		this.mappers = mappers;
	}

	/**
	 * 取得指定外部系統所對應的 Exception Mapper。
	 *
	 * @param system 外部系統識別名稱
	 * @return 對應的 {@link ExternalExceptionMapper}
	 * @throws NoSuchElementException 若未註冊任何可支援的 Mapper（理論上不應發生）
	 */
	public ExternalExceptionMapper get(String system) {
		// 理論上不會發生，因應至少有 Default Mapper
		return mappers.stream().filter(m -> m.supports(system)).findFirst().orElseThrow();
	}
}
