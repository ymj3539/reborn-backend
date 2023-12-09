package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Pay;
import com.rainbowbridge.reborn.domain.Pet;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.reservation.ReservationAddRequestDto;
import com.rainbowbridge.reborn.dto.reservation.UpcomingReservationResponseDto;
import com.rainbowbridge.reborn.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CommonService commonService;

    public UpcomingReservationResponseDto getUpcomingReservation(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return null;
        }

        List<Reservation> reservations = reservationRepository.findAllByUser(user);

        if (reservations.isEmpty()) {
            return null;
        }

        Reservation upcomingReservation = reservations.get(reservations.size() - 1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(upcomingReservation.getDate(), LocalTime.of(upcomingReservation.getTime(), 0));

        if (reservationDateTime.isBefore(now)) {
            return null;
        }

        return UpcomingReservationResponseDto.builder()
                .companyName(upcomingReservation.getCompany().getName())
                .reservationDate(commonService.convertLocalDateFormat(upcomingReservation.getDate()))
                .reservationTime(commonService.convertTimeFormat(upcomingReservation.getTime()))
                .build();

    }

    public Reservation addReservation(ReservationAddRequestDto reservationDto, Pet pet, Pay pay, User user, Product product, Company company) {
        return reservationRepository.save(reservationDto.toEntity(pet, pay, user, product, company));
    }

}
