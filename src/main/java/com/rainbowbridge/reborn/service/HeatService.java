package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Heart;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class HeatService {

    private final HeartRepository heartRepository;
    private final CompanyService companyService;

    public void addHeart(String companyId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Company company = companyService.getCompany(companyId);
        heartRepository.save(new Heart(user, company));
    }
}
