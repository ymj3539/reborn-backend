package com.rainbowbridge.reborn.domain;

public enum Payment {

    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이"),
    CARD("신용/체크카드");

    private final String displayName;

    Payment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
