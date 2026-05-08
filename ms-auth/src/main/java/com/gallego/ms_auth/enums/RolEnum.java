package com.gallego.ms_auth.enums;

public enum RolEnum {
    ADMIN(1L),
    CLIENTE(2L);

    private final Long id;

    RolEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
