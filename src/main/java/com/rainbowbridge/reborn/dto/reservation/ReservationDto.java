package com.rainbowbridge.reborn.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long reservationId;

    private boolean reservationYN;

    private String reservationDate;

    private String bundleName;
}