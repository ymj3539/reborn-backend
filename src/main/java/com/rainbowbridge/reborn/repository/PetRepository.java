package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
