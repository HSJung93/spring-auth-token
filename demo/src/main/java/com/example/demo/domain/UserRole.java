package com.example.demo.domain;

public enum UserRole {
    ROLE_NOT_PERMITTED(0),
    ROLE_USER(1),
    ROLE_MANAGER(2),
    ROLE_ADMIN(3);

    private final int number;

    UserRole(final int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}
