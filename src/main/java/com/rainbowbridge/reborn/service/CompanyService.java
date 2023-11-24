package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.ProductType;
import com.rainbowbridge.reborn.dto.company.CalendarCompanyListRequestDto;
import com.rainbowbridge.reborn.dto.company.CompanyListDto;
import com.rainbowbridge.reborn.dto.product.CalendarProductResponseDto;
import com.rainbowbridge.reborn.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyListDto> getCalendarCompanyList(CalendarCompanyListRequestDto dto) {
        // 영업 가능한 업체 조회
        List<Company> availableCompanies = companyRepository.findAvailableCompanieList(dto.getSelectedDate(), dto.getSelectedTime());
        // 가까운 순 10개 업체 계산
        List<Company> nearbyAvailableCompanies = calculateNearbyCompanyList(availableCompanies, dto.getUserLatitude(), dto.getUserLongitude());

        List<CompanyListDto> companyResponseDtoList = new ArrayList<>();

        for (Company company : nearbyAvailableCompanies) {

            // 리본 패키지만 추출
            List<CalendarProductResponseDto> productResponseDtoList = Optional.ofNullable(company.getProducts())
                    .orElse(Collections.emptyList()).stream()
                    .filter(product -> product.getProductType().equals(ProductType.REBORN_PACKAGE))
                    .map(product -> CalendarProductResponseDto.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .build())
                    .collect(Collectors.toList());

            CompanyListDto companyListResponseDto = CompanyListDto.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .products(productResponseDtoList)
                    .build();

            companyResponseDtoList.add(companyListResponseDto);
        }

        return companyResponseDtoList;
    }

    private List<Company> calculateNearbyCompanyList(List<Company> companies, double userLatitude, double userLongitude) {
        // 각 업체의 사용자의 거리를 계산하고, 그 결과를 Map에 저장
        Map<Company, Double> companyDistanceMap = new HashMap<>();
        for (Company company : companies) {
            double distance = calculateDistance(userLatitude, userLongitude, company.getLatitude(), company.getLongitude());
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

    // Vincenty 공식 사용
    private double calculateDistance(double userLatitude, double userLongitude, double companyLatitude, double companyLongitude) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563;  // WGS-84 ellipsoid params
        double L = Math.toRadians(companyLongitude - userLongitude);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(userLatitude)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(companyLatitude)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double lambda = L, lambdaP, iterLimit = 100;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) return 0;  // co-incident points
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM)) cos2SigmaM = 0;  // equatorial line: cosSqAlpha=0 (§6)
            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0) return 0;  // formula failed to converge

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double s = b * A * (sigma - deltaSigma);

        return s;
    }
}
