package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Pet;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.pet.PetAddRequestDto;
import com.rainbowbridge.reborn.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

    private final PetRepository petRepository;

    public Pet addPet(PetAddRequestDto dto, User user) {
        return petRepository.save(dto.toEntity(user));
    }

}
