package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Pay;
import com.rainbowbridge.reborn.dto.pay.PayAddRequestDto;
import com.rainbowbridge.reborn.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;

    public Pay addPay(PayAddRequestDto payDto) {
        return payRepository.save(payDto.toEntity());
    }

}
