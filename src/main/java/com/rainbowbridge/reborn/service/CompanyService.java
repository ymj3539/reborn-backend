package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.ProductType;
import com.rainbowbridge.reborn.domain.Region;
import com.rainbowbridge.reborn.domain.Review;
import com.rainbowbridge.reborn.domain.SortCriteria;
import com.rainbowbridge.reborn.dto.company.CalendarCompanyListRequestDto;
import com.rainbowbridge.reborn.dto.company.CompanyListDto;
import com.rainbowbridge.reborn.dto.company.FilteredCompanyListRequestDto;
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
    private final CommonService commonService;

    public List<CompanyListDto> getCalendarCompanyList(CalendarCompanyListRequestDto dto) {
        // 영업 가능한 업체 조회
        List<Company> availableCompanies = companyRepository.findAvailableCompanieList(dto.getSelectedDate(), dto.getSelectedTime());
        // 가까운 순으로 업체 정렬
        List<Company> sortedCompanies = sortCompanyListByDistance(availableCompanies, dto.getUserLatitude(), dto.getUserLongitude());
        // 가장 가까운 10개 업체만 선택.
        List<Company> nearbyAvailableCompanies = sortedCompanies.size() > 10 ? sortedCompanies.subList(0, 10) : sortedCompanies;

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

    public List<CompanyListDto> getFilteredCompanyList(FilteredCompanyListRequestDto dto) {
        List<Company> companies;

        // 지역권 기준으로 업체 리스트 조회
        if (dto.getRegion().equals(Region.ALL)) {
            companies = companyRepository.findAll();
        }
        else {
            companies = companyRepository.findAllByRegion(dto.getRegion().getDisplayName());
        }

        // 정렬 기준으로 업체 리스트 정렬
        SortCriteria sortCriteria = dto.getSortCriteria();
        if (sortCriteria.equals(SortCriteria.RATING)) {
            sortCompaniesByAverageRating(companies);
        }
        else if (sortCriteria.equals(SortCriteria.POPULARITY)) {
            sortCompaniesByReservationCount(companies);
        }

        List<CompanyListDto> companyResponseDtoList = new ArrayList<>();

        for (Company company : companies) {

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

    private List<Company> sortCompanyListByDistance(List<Company> companies, double userLatitude, double userLongitude) {
        // 각 업체의 사용자의 거리를 계산하고, 그 결과를 Map에 저장
        Map<Company, Double> companyDistanceMap = new HashMap<>();
        for (Company company : companies) {
            double distance = commonService.calculateDistance(userLatitude, userLongitude, company.getLatitude(), company.getLongitude());
            companyDistanceMap.put(company, distance);
        }

        // Map의 값을 기준으로 가장 가까운 순으로 정렬
        List<Map.Entry<Company, Double>> entries = new ArrayList<>(companyDistanceMap.entrySet());
        entries.sort(Map.Entry.comparingByValue());

        return entries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void sortCompaniesByAverageRating(List<Company> companies) {
        companies.sort((c1, c2) -> {
            double avgRating1 = c1.getReviews()
                    .stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            double avgRating2 = c2.getReviews()
                    .stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            return Double.compare(avgRating2, avgRating1);  // 내림차순 정렬
        });
    }

    private void sortCompaniesByReservationCount(List<Company> companies) {
        companies.sort((c1, c2) -> {
            int count1 = c1.getReservations().size();
            int count2 = c2.getReservations().size();
            return Integer.compare(count2, count1);
        });
    }

}
