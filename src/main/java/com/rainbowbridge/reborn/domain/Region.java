package com.rainbowbridge.reborn.domain;

public enum Region {

    CAPITAL("수도권"),
    CHUNGCHEONG("충청권"),
    GANGWON("강원권"),
    YEONGNAM("영남권"),
    HONAM("호남권");

    private final String displayName;

    Region(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
