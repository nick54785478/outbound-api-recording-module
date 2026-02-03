package com.example.demo.infra.outbound.feign.decoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.infra.context.ContextHolder;
import com.example.demo.infra.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.shared.exception.CustomFeignException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final OutboundApiResponseValidatorFactory validatorFactory;

    public FeignErrorDecoder(OutboundApiResponseValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        OutboundApiRequestInfo context = ContextHolder.getFeignContext();

        String body = null;
        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            log.warn("Feign response body 讀取失敗", e);
        }

        // 若 HTTP 狀態碼非 2xx，直接拋出 Exception
        if (response.status() < 200 || response.status() >= 300) {
            return new CustomFeignException(
                "HTTP_" + response.status(),
                "HTTP Error: " + response.status() + ", body=" + body
            );
        }

        // 對成功 HTTP 回應做額外檢核
        try {
            validatorFactory.get(context.getSystem()).validate(body, context);
        } catch (RuntimeException e) {
            return e;
        }

        // 回應成功且 Validator 沒拋例外
        return null; // Feign 會正常返回 response body
    }
}
