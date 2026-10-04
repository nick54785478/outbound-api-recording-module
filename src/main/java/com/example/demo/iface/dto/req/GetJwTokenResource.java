package com.example.demo.iface.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetJwTokenResource {

	private String tenant;

	private String username;
	
	private String password;
}
