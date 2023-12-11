package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.reservation.UpcomingReservationResponseDto;
import com.rainbowbridge.reborn.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/reservations", produces = "application/json; charset=utf8")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/upcoming")
    public UpcomingReservationResponseDto getUpcomingReservation(@RequestParam(required = false, defaultValue = "") String userId) {
        return reservationService.getUpcomingReservation(userId);
    }

}
