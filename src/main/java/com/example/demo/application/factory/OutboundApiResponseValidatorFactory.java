package com.example.demo.application.factory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.example.demo.application.port.OutboundApiResponseValidatorPort;
import com.example.demo.application.shared.outbound.context.OutboundApiRequestInfo;

/**
 * 外部系統回應檢核器 Factory（使用 Map 存取）
 *
 * <p>
 * 依系統代碼選擇對應的 Validator Adapter，完全不用 filter。
 * 此為純 POJO 設計，依賴由外部 Configuration 負責傳入。
 * </p>
 */
public class OutboundApiResponseValidatorFactory {

	private final Map<String, OutboundApiResponseValidatorPort> validatorMap;

	/**
	 * 建構函數：接收 Validator List 並轉成 Map，key 為 system()
	 *
	 * @param validators 所有 Validator 實作
	 */
	public OutboundApiResponseValidatorFactory(List<OutboundApiResponseValidatorPort> validators) {
		this.validatorMap = validators.stream().collect(Collectors
				.toMap(OutboundApiResponseValidatorPort::supportSystem, v -> v, (existing, replacement) -> existing));
		// 註. 若重複 key 保留第一個
	}

	/**
	 * 取得對應系統的 Validator
	 *
	 * @param system 外部系統名稱
	 * @return 對應 Validator；找不到回傳預設放行 Validator
	 */
	public OutboundApiResponseValidatorPort get(String system) {
		return validatorMap.getOrDefault(system, defaultValidator());
	}

	/**
	 * 預設 Validator：什麼都不做
	 */
	private OutboundApiResponseValidatorPort defaultValidator() {
		return new OutboundApiResponseValidatorPort() {
			@Override
			public String supportSystem() {
				return "DEFAULT";
			}

			@Override
			public void validate(Object response, OutboundApiRequestInfo context) {
				// 放行
			}
		};
	}
}
