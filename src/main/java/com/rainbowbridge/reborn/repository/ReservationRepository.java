package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByUser(User user);
    List<Reservation> findAllByUserAndCompany(User user, Company company);
}
