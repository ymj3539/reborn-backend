package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.pay.PayAddRequestDto;
import com.rainbowbridge.reborn.dto.pay.PayAddResponseDto;
import com.rainbowbridge.reborn.dto.pet.PetAddRequestDto;
import com.rainbowbridge.reborn.dto.reservation.ReservationAddRequestDto;
import com.rainbowbridge.reborn.service.PayService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pays")
public class PayController {

    private final PayService payService;

    @PostMapping
    @ApiOperation(value = "결제 하기")
    public PayAddResponseDto add(@RequestBody PayAddRequestDto payDto,
                                 @RequestBody ReservationAddRequestDto reservationDto,
                                 @RequestBody PetAddRequestDto petDto,
                                 HttpSession session) {
        return payService.addPayAndReservationAndPet(payDto, reservationDto, petDto, session);
    }

}
