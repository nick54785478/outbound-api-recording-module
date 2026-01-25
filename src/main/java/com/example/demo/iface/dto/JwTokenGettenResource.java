package com.example.demo.iface.dto;

import com.example.demo.application.shared.outbound.auth.dto.JwTokenGettenData;

public record JwTokenGettenResource(String code, String message, JwTokenGettenData data) {

}
