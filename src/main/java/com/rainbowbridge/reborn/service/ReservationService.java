package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.reservation.CheckReservationResponseDto;
import com.rainbowbridge.reborn.dto.reservation.ReservationDto;
import com.rainbowbridge.reborn.dto.reservation.UpcomingReservationResponseDto;
import com.rainbowbridge.reborn.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public CheckReservationResponseDto checkReservation(Company company, String token) {
        User user = userService.checkUser(token);

        if (user == null) {
            return null;
        }

        List<Reservation> reservations = reservationRepository.findAllByUserAndCompany(user, company);

        if (reservations.isEmpty()) {
            return toCheckReservationResponseDto(false, null);
        }

        Reservation upcomingReservation = reservations.get(reservations.size() - 1);

        if (isReservationExpired(upcomingReservation)) {
            return toCheckReservationResponseDto(false, null);
        }

        return toCheckReservationResponseDto(true, Utils.convertLocalDateFormat(upcomingReservation.getDate()));
    }

    @Transactional(readOnly = true)
    public CheckReservationResponseDto checkReservation(Company company, User user) {

        List<Reservation> reservations = reservationRepository.findAllByUserAndCompany(user, company);

        if (reservations.isEmpty()) {
            return toCheckReservationResponseDto(false, null);
        }

        Reservation upcomingReservation = reservations.get(reservations.size() - 1);

        if (isReservationExpired(upcomingReservation)) {
            return toCheckReservationResponseDto(false, null);
        }

        return toCheckReservationResponseDto(true, Utils.convertLocalDateFormat(upcomingReservation.getDate()));
    }


    @Transactional(readOnly = true)
    public ReservationDto findRecentReservationDto(Company company, User user) {

        List<Reservation> reservations = reservationRepository.findAllByUserAndCompany(user, company);

        if (reservations.isEmpty()) {
            return (new ReservationDto(null, false, null, null));
        }

        Reservation upcomingReservation = reservations.get(reservations.size() - 1);

        if (isReservationExpired(upcomingReservation)) {
            return (new ReservationDto(null, false, null, null));
        }

        return ReservationDto.builder()
                .reservationId(upcomingReservation.getId())
                .reservationYN(true)
                .reservationDate(Utils.convertLocalDateFormat(upcomingReservation.getDate()))
                .bundleName(upcomingReservation.getBundle().getName())
                .build();
    }

    @Transactional(readOnly = true)
    public UpcomingReservationResponseDto getUpcomingReservation(String token) {
        User user = userService.checkUser(token);

        if (user == null) {
            return null;
        }

        List<Reservation> reservations = reservationRepository.findAllByUser(user);

        if (reservations.isEmpty()) {
            return null;
        }

        Reservation upcomingReservation = reservations.get(reservations.size() - 1);

        if (isReservationExpired(upcomingReservation)) {
            return null;
        }

        return UpcomingReservationResponseDto.builder()
                .imagePath(Utils.getImagePath(upcomingReservation.getCompany().getNickname()))
                .companyName(upcomingReservation.getCompany().getName())
                .bundleName(upcomingReservation.getBundle().getName())
                .payDt(Utils.convertLocalDateTimeFormat(upcomingReservation.getPay().getPayDt()))
                .totalPrice(upcomingReservation.getPay().getTotalPrice())
                .reservationDate(Utils.convertLocalDateFormat(upcomingReservation.getDate()))
                .reservationTime(Utils.convertTimeFormat(upcomingReservation.getTime()))
                .build();

    }

    private boolean isReservationExpired(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), LocalTime.of(reservation.getTime(), 0));
        return reservationDateTime.isBefore(now);
    }

    private CheckReservationResponseDto toCheckReservationResponseDto(boolean reservationYN, String reservationDate) {
        return CheckReservationResponseDto.builder()
                .reservationYN(reservationYN)
                .reservationDate(reservationDate)
                .build();
    }

}
