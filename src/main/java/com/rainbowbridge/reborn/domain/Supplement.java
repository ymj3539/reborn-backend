package com.rainbowbridge.reborn.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                // 이름

    private String intro;               // 설명

    private int price;                  // 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "supplement", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "supplement", cascade = CascadeType.ALL)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "supplement", cascade = CascadeType.ALL)
    private List<Supplement_Reservation> supplementReservations = new ArrayList<>();

}
