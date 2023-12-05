package com.rainbowbridge.reborn.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Payment payment;        // 결제 수단

    @Enumerated(EnumType.STRING)
    private Card card;              // 결제 카드

    private int installmentMonths;  // 할부 개월 수 : 일시불 = 0

    @CreatedDate
    private LocalDateTime payDt;    // 결제 일시

    private int totalPrice;         // 총 결제 금액

    @OneToOne(mappedBy = "pay", cascade = CascadeType.ALL)
    private Reservation reservation;

}
