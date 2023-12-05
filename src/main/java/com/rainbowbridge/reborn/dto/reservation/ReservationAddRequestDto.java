package com.rainbowbridge.reborn.dto.reservation;

import com.rainbowbridge.reborn.domain.Card;
import com.rainbowbridge.reborn.domain.Payment;
import com.rainbowbridge.reborn.domain.Species;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAddRequestDto {

    @NotNull
    @ApiModelProperty(value = "예약 일자", example = "2023-12-05")
    private LocalDate reservationDate;

    @NotNull
    @ApiModelProperty(value = "예약 시간", example = "18")
    private int reservationTime;

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

}
