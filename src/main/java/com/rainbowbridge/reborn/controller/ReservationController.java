package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.reservation.UpcomingReservationResponseDto;
import com.rainbowbridge.reborn.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/upcoming")
    public UpcomingReservationResponseDto getUpcomingReservation(HttpSession session) {
        return reservationService.getUpcomingReservation(session);
    }

}
