package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

}
