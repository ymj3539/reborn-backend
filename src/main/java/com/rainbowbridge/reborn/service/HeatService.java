package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Heart;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.heart.HeartListDto;
import com.rainbowbridge.reborn.repository.CompanyRepository;
import com.rainbowbridge.reborn.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeatService {

    private final CompanyRepository companyRepository;
    private final HeartRepository heartRepository;

    public boolean check(Company company, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return false;
        }

        return heartRepository.findAllByUserAndCompany(user, company).size() > 0;
    }

    public List<HeartListDto> getHeartList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return null;
        }

        List<Heart> hearts = heartRepository.findAllByUser(user);

        List<HeartListDto> dtos = new ArrayList<>();

        if (!hearts.isEmpty()) {
            List<Company> heartCompanies = hearts.stream()
                    .map(Heart::getCompany)
                    .collect(Collectors.toList());

            dtos = heartCompanies.stream()
                    .map(company -> HeartListDto.builder()
                            .id(company.getId())
                            .name(company.getName())
                            .address(company.getAddress())
                            .businessHours(Utils.convertTimeRangeFormat(company.getOpenTime(), company.getCloseTime()))
                            .imagePath(Utils.getImagePath(company.getId()))
                            .build())
                    .collect(Collectors.toList());
        }

        return dtos;
    }
}
