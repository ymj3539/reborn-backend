package com.rainbowbridge.reborn.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Species species;    // 종

    private String breed;       // 품종

    private String name;        // 이름

    private int years;          // 나이 - 년

    private int months;         // 나이 - 개월

    private double weight;      // 체중

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    private Reservation reservation;

    @Builder
    public Pet(Species species, String breed, String name, int years, int months, double weight, User user) {
        this.species = species;
        this.breed = breed;
        this.name = name;
        this.years = years;
        this.months = months;
        this.weight = weight;
        this.user = user;
    }

}
