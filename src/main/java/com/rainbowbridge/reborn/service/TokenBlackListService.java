package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.TokenBlackList;
import com.rainbowbridge.reborn.repository.TokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;

    @Transactional(readOnly = true)
    public boolean check(String accessToken) {
        return tokenBlackListRepository.existsByAccessToken(accessToken);
    }

    public void add(String accessToken, Date expiryDate) {
        TokenBlackList blackListToken = new TokenBlackList();
        blackListToken.setAccessToken(accessToken);
        blackListToken.setExpiryDate(expiryDate);
        tokenBlackListRepository.save(blackListToken);
    }

    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 만료된 토큰 삭제
    public void cleanup() {
        Date now = new Date();
        tokenBlackListRepository.deleteAllByExpiryDateBefore(now);
    }

}
