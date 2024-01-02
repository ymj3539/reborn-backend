package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {
    boolean existsByAccessToken(String accessToken);

    void deleteAllByExpiryDateBefore(Date now);
}
