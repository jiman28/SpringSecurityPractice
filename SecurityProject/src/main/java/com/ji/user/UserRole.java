package com.ji.user;

public enum UserRole {
	
	ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;

    // getter only (상수 자료형이므로)
    
	public String getValue() {
		return value;
	}

}
