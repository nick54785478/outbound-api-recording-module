package com.example.demo.application.shared.outbound.auth.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetJwTokenCommand {

	private String username;

	private String password;
}
