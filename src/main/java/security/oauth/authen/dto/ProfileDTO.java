package security.oauth.authen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {

	private String username;
	private String phone;
	private String firstName;
	private String lastName;
	private String address;
	private String otp;
}
