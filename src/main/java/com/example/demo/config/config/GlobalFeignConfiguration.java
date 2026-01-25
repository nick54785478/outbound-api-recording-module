package com.example.demo.config.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.infra.outbound.exception.mapper.ExternalExceptionMapper;
import com.example.demo.infra.outbound.feign.decoder.FeignErrorDecoder;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 全域 Feign Client 設定。
 *
 * <p>
 * 此組態類別用於定義「所有 Feign Client 共用」的基礎行為設定， 例如錯誤處理、攔截器、編解碼策略等。
 * </p>
 *
 * <p>
 * 本設定會被 Spring Cloud OpenFeign 掃描並套用至所有未指定 專屬 configuration 的 Feign Client。
 * </p>
 *
 * <p>
 * 目前職責：
 * <ul>
 * <li>註冊全域 {@link feign.codec.ErrorDecoder}</li>
 * <li>統一外部 API 呼叫失敗時的 Exception 轉換行為</li>
 * </ul>
 * </p>
 *
 * <p>
 * 設計原則：
 * <ul>
 * <li>不放置任何特定外部系統的邏輯</li>
 * <li>僅負責「機制」而非「策略」</li>
 * <li>實際錯誤轉換策略交由 {@link ExternalExceptionMapper} 系列實作處理</li>
 * </ul>
 * </p>
 *
 * <p>
 * 若某 Feign Client 需要客製行為（例如特殊的 ErrorDecoder 或攔截器）， 可於
 * {@code @FeignClient(configuration = XxxFeignConfiguration.class)}
 * 指定專屬設定，將不會套用此全域設定。
 * </p>
 */
@Slf4j
@Configuration
public class GlobalFeignConfiguration {
	

    @Bean
    public ErrorDecoder feignErrorDecoder(OutboundApiResponseValidatorFactory validatorFactory) {
        return new FeignErrorDecoder(validatorFactory);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
