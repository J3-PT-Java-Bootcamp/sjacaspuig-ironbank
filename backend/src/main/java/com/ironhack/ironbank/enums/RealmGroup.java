package com.ironhack.ironbank.enums;

public enum RealmGroup {
    ADMINS("admins"),
    USERS("users");

    private String name;

    RealmGroup(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
