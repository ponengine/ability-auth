package security.oauth.authen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {

	private String username;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String address;
}
