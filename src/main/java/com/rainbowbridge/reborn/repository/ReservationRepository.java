package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
