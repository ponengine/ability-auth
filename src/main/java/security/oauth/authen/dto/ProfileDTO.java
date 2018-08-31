package security.oauth.authen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {

	private String username;
	private String phone;
	private String otp;
	private String password;
	private String role;
	private String userType;
	
}
