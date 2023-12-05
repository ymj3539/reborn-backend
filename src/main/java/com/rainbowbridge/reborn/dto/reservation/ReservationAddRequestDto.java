package com.rainbowbridge.reborn.dto.reservation;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ApiModelProperty(value = "상품 ID", example = "500000")
    private long product_id;

}
