package security.oauth.authen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

	private String username;
	private String password;
	private String newPassword;
}
