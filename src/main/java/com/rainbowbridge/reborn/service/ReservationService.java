package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Pay;
import com.rainbowbridge.reborn.domain.Pet;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.reservation.CheckReservationResponseDto;
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

    public CheckReservationResponseDto checkReservation(Company company, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return null;
        }

        List<Reservation> reservations = reservationRepository.findAllByUserAndCompany(user, company);

        if (reservations.isEmpty()) {
            return toCheckReservationResponseDto(false, null);
        }

        Reservation upcomingReservation = reservations.get(reservations.size() - 1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(upcomingReservation.getDate(), LocalTime.of(upcomingReservation.getTime(), 0));

        if (reservationDateTime.isBefore(now)) {
            return toCheckReservationResponseDto(false, null);
        }

        return toCheckReservationResponseDto(true, Utils.convertLocalDateFormat(upcomingReservation.getDate()));
    }

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
                .productName(upcomingReservation.getProduct().getName())
                .payDt(Utils.convertLocalDateTimeFormat(upcomingReservation.getPay().getPayDt()))
                .totalPrice(upcomingReservation.getPay().getTotalPrice())
                .reservationDate(Utils.convertLocalDateFormat(upcomingReservation.getDate()))
                .reservationTime(Utils.convertTimeFormat(upcomingReservation.getTime()))
                .build();

    }

    public Reservation addReservation(ReservationAddRequestDto reservationDto, Pet pet, Pay pay, User user, Product product, Company company) {
        return reservationRepository.save(reservationDto.toEntity(pet, pay, user, product, company));
    }

    private CheckReservationResponseDto toCheckReservationResponseDto(boolean reservationYN, String reservationDate) {
        return CheckReservationResponseDto.builder()
                .reservationYN(reservationYN)
                .reservationDate(reservationDate)
                .build();
    }

}
