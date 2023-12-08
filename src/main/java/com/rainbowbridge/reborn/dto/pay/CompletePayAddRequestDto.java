package com.rainbowbridge.reborn.dto.pay;

import com.rainbowbridge.reborn.dto.pet.PetAddRequestDto;
import com.rainbowbridge.reborn.dto.reservation.ReservationAddRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletePayAddRequestDto {

    private PayAddRequestDto payDto;
    private ReservationAddRequestDto reservationDto;
    private PetAddRequestDto petDto;

}
