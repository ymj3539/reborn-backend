package com.rainbowbridge.reborn.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingReservationResponseDto {

    private String imagePath;

    private String companyName;

    private String productName;

    private String payDt;

    private int totalPrice;

    private String reservationDate;

    private String reservationTime;

}
