package com.example.demo.iface.resource.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取得 JWT Token 請求載體
 *
 * <p>符合命名規範 3-1: V + N + Resource，放置於 resource/in/</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetJwTokenResource {

	private String tenant;

	private String username;

	private String password;
}
