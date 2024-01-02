package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.TokenBlackList;
import com.rainbowbridge.reborn.repository.TokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;

    public void add(String accessToken, Date expiryDate) {
        TokenBlackList blackListToken = new TokenBlackList();
        blackListToken.setAccessToken(accessToken);
        blackListToken.setExpiryDate(expiryDate);
        tokenBlackListRepository.save(blackListToken);
    }

}
