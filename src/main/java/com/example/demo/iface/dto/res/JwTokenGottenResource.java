package com.example.demo.iface.dto.res;

import com.example.demo.application.shared.dto.JwTokenGottenResult;

public record JwTokenGottenResource(String code, String message, JwTokenGottenResult data) {

}
