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

    private String companyName;

    private String reservationDate;

    private String reservationTime;

}
