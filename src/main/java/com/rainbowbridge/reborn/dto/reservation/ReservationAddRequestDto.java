package com.rainbowbridge.reborn.dto.reservation;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Pay;
import com.rainbowbridge.reborn.domain.Pet;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
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
    @ApiModelProperty(value = "상품 ID", example = "1")
    private long product_id;

    public Reservation toEntity(Pet pet, Pay pay, User user, Product product, Company company) {
        return Reservation.builder()
                .date(reservationDate)
                .time(reservationTime)
                .pet(pet)
                .pay(pay)
                .user(user)
                .company(company)
                .product(product)
                .build();
    }
}
