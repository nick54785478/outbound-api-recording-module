package com.example.demo.iface.dto.res;

import com.example.demo.application.shared.outbound.auth.dto.JwTokenGottenData;

public record JwTokenGettenResource(String code, String message, JwTokenGottenData data) {

}
