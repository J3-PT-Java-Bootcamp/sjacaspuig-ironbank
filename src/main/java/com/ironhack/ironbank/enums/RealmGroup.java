package com.ironhack.ironbank.enums;

public enum RealmGroup {
    ADMINS("ADMINS"),
    USERS("USERS");

    private String name;

    RealmGroup(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
