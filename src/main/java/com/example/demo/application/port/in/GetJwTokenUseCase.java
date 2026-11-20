package com.example.demo.application.port.in;

import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.command.GetJwTokenCommand;

/**
 * 取得 JWT Token UseCase
 *
 * <p>符合命名規範 3-8: in: ... + UseCase</p>
 */
public interface GetJwTokenUseCase {

	/**
	 * 執行向 Auth Service 登入並取得 JWT Token
	 *
	 * @param command 包含使用者名稱與密碼的 Command
	 * @return 包含 Token 的 Result DTO
	 */
	JwTokenGottenResult getJwToken(GetJwTokenCommand command);
}
