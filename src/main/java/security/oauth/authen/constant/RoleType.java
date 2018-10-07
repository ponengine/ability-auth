package security.oauth.authen.constant;

public enum RoleType {
	ROLE_USER("U"),
	ROLE_CUSTOMER("C"),
	ROLE_ADMIN("A");
	
	private String role;

	public String getRole() {
		return role;
	}

	private RoleType(String role) {
		this.role = role;
	}
}
