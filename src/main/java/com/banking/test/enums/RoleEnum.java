package com.banking.test.enums;

public enum RoleEnum {

    ROLE_USER("ROLE_USER");

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getRoleValue() { return value.substring(5); }
}
