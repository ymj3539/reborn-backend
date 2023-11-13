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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String target;              // 대상 : 5kg 이상, 10kg 이상 ...

    @Enumerated(EnumType.STRING)
    private ProductType productType;    // 타입 : 리본패키지, 업체패키지, 옵션 상품

    private String name;                // 이름

    private String intro;               // 설명

    private int price;                  // 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "productPics", cascade = CascadeType.ALL)
    private List<File> pics = new ArrayList<>();

}
