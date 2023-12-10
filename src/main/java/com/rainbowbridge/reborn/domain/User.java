package com.rainbowbridge.reborn.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String password;    // 비밀번호

    private String name;        // 이름

    private String phoneNum;    // 전화번호

    private LocalDate birthday; // 생일

    @Enumerated(EnumType.STRING)
    private Gender gender;      // 성별

    private String address;     // 주소

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatContent> chatContents = new ArrayList<>();

    @Builder
    public User(String id, String password, String name, String phoneNum, LocalDate birthday, Gender gender, String address) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
    }

}
