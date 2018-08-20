package security.oauth.authen.constant;

public enum UserType {
	BLACKLIST("B"),
	INACTIVE("I"),
	ACTIVE("A");
	
	private String type;

	public String getType() {
		return type;
	}

	private UserType(String type) {
		this.type = type;
	}
}
