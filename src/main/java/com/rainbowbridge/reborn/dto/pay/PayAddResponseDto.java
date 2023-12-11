package com.rainbowbridge.reborn.dto.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayAddResponseDto {

    @ApiModelProperty(value = "결제 일시", example = "")
    private String payDt;

    @ApiModelProperty(value = "예약 일자", example = "11.15(수)")
    private String reservationDate;

    @ApiModelProperty(value = "예약 시간", example = "오전 10:00")
    private String reservationTime;

    @ApiModelProperty(value = "업체명", example = "포포즈 경기 김포점")
    private String companyName;

    @ApiModelProperty(value = "업체 이미지 경로", example = "")
    private String companyImagePath;

    @ApiModelProperty(value = "상품명", example = "포포즈 패키지")
    private String productName;

    @ApiModelProperty(value = "총 결제 금액", example = "500000")
    private int totalPrice;

}
