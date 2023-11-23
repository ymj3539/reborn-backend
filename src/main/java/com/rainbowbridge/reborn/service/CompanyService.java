package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.dto.company.CalendarCompanyListRequestDto;
import com.rainbowbridge.reborn.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> getCalendarCompanyList(CalendarCompanyListRequestDto dto) {
        // 영업 가능한 업체 조회
        List<Company> availableCompanies = companyRepository.findAvailableCompanieList(dto.getSelectedDate(), dto.getSelectedTime());
        // 가까운 순 10개 업체 계산
        return calculateNearbyCompanyList(availableCompanies, dto.getUserLatitude(), dto.getUserLongitude());
    }

    private List<Company> calculateNearbyCompanyList(List<Company> companies, double userLatitude, double userLongitude) {
        // 각 업체의 사용자의 거리를 계산하고, 그 결과를 Map에 저장
        Map<Company, Double> companyDistanceMap = new HashMap<>();
        for (Company company : companies) {
            double distance = calculateDistance(userLatitude, userLongitude, company.getLatitude(), company.getLatitude());
            companyDistanceMap.put(company, distance);
        }

        // Map의 값을 기준으로 가장 가까운 순으로 정렬
        List<Map.Entry<Company, Double>> entries = new ArrayList<>(companyDistanceMap.entrySet());
        entries.sort(Map.Entry.comparingByValue());

        // 상위 10개 업체만 선택.
        List<Company> nearestCompanies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            nearestCompanies.add(entries.get(i).getKey());
        }

        return nearestCompanies;
    }

    // Haversine 공식 사용
    private double calculateDistance(double userLatitude, double userLongitude, double companyLatitude, double companyLongitude) {
        int radiusOfEarth = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(companyLatitude - userLatitude);
        double lonDistance = Math.toRadians(companyLongitude - userLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(companyLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radiusOfEarth * c; // convert to kilometers

        return distance;
    }

}
