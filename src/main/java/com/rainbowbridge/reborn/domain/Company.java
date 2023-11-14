package com.rainbowbridge.reborn.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Company {

    @Id
    private String id;

    private String password;        // 비밀번호

    private String name;            // 이름

    @Enumerated(EnumType.STRING)
    private Region region;          // 지역

    @Enumerated(EnumType.STRING)
    private Area area;              // 세부지역

    private String address;         // 주소

    private String intro;           // 설명

    private String telNum;          // 전화번호

    private int openTime;           // 영업시작시간

    private int closeTime;          // 영업마감시간

    private String notification;    // 안내사항

    private boolean pickupYn;       // 픽업 서비스 제공 여부

    @OneToOne(mappedBy = "companyMain", cascade = CascadeType.ALL)
    private File mainPic;

    @OneToMany(mappedBy = "company")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "companyPics", cascade = CascadeType.ALL)
    private List<File> pics = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<TimeOff> timeOffs = new ArrayList<>();
}
