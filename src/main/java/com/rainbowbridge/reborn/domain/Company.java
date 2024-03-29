package com.rainbowbridge.reborn.domain;

import com.rainbowbridge.reborn.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String password;        // 비밀번호

    private String name;            // 이름

    private String region;          // 권역

    private String postalCode;      // 우편번호

    private String baseAddress;     // 기본 주소

    private String detailAddress;   // 상세 주소

    private String intro;           // 설명

    private String telNum;          // 전화번호

    private int openTime;           // 영업시작시간

    private int closeTime;          // 영업마감시간

    private String notification;    // 안내사항

    private double latitude;        // 위도

    private double longitude;       // 경도

    private boolean approved;       // 입점 승인 여부

    @OneToOne(mappedBy = "companyMain", cascade = CascadeType.ALL)
    private File mainFile;

    @OneToMany(mappedBy = "company")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Bundle> bundles = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Supplement> supplements = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<TimeOff> timeOffs = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public double getAverageRating() {
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
