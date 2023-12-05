package com.rainbowbridge.reborn.domain;

public enum Card {

    KOOKMIN_CARD("국민카드"),
    SAMSUNG_CARD("삼성카드"),
    LOTTE_CARD("롯데카드"),
    WOORI_CARD("우리카드"),
    SHINHAN_CARD("신한카드"),
    HYUNDAI_CARD("현대카드");

    private final String displayName;

    Card(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
