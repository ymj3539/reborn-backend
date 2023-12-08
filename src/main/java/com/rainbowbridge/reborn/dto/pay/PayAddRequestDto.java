package com.rainbowbridge.reborn.dto.pay;

import com.rainbowbridge.reborn.domain.Card;
import com.rainbowbridge.reborn.domain.Pay;
import com.rainbowbridge.reborn.domain.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayAddRequestDto {

    @NotNull
    @ApiModelProperty(value = "결제 수단", example = "KAKAO_PAY")
    private Payment payment;

    @NotNull
    @ApiModelProperty(value = "결제 카드", example = "KOOKMIN_CARD")
    private Card card;

    @NotNull
    @ApiModelProperty(value = "할부 개월 수; 일시불 = 0", example = "3")
    private int installmentMonths;

    @NotNull
    @ApiModelProperty(value = "총 결제 금액", example = "500000")
    private int totalPrice;

    public Pay toEntity() {
        return Pay.builder()
                .payment(payment)
                .card(card)
                .installmentMonths(installmentMonths)
                .totalPrice(totalPrice)
                .build();
    }
}
