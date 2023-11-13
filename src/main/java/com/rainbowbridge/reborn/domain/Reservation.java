package com.rainbowbridge.reborn.domain;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;         // 예약 일자

    private int time;               // 예약 시간

    @Enumerated(EnumType.STRING)
    private Payment payment;        // 결제 수단

    @Enumerated(EnumType.STRING)
    private Card card;              // 결제 카드

    private int installmentMonths;  // 할부 개월 수 : 일시불 = 0

    private LocalDateTime payDt;    // 결제 일시

    private int totalPrice;         // 총 결제 금액

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Review review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
