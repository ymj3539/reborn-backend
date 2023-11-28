package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.ProductType;
import com.rainbowbridge.reborn.domain.Region;
import com.rainbowbridge.reborn.domain.Review;
import com.rainbowbridge.reborn.domain.SortCriteria;
import com.rainbowbridge.reborn.dto.company.CompanyListDto;
import com.rainbowbridge.reborn.dto.product.RebornPackageListDto;
import com.rainbowbridge.reborn.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<CompanyListDto> getCalendarCompanyList(LocalDate selectedDate, int selectedTime, double userLatitude, double userLongitude) {
        // 영업 가능한 업체 조회
        List<Company> availableCompanies = companyRepository.findAvailableCompanieList(selectedDate, selectedTime);
        // 가까운 순으로 업체 정렬
        List<Company> sortedCompanies = sortCompanyListByDistance(availableCompanies, userLatitude, userLongitude);
        // 가장 가까운 10개 업체만 선택.
        List<Company> nearbyAvailableCompanies = sortedCompanies.size() > 10 ? sortedCompanies.subList(0, 10) : sortedCompanies;

        return toCompanyListDto(nearbyAvailableCompanies);
    }

    public List<CompanyListDto> getFilteredCompanyList(SortCriteria sortCriteria, Region region) {
        List<Company> companies;

        // 지역권 기준으로 업체 리스트 조회
        if (region.equals(Region.ALL)) {
            companies = companyRepository.findAll();
        }
        else {
            companies = companyRepository.findAllByRegion(region.getDisplayName());
        }

        // 정렬 기준으로 업체 리스트 정렬
        if (sortCriteria.equals(SortCriteria.RATING)) {
            sortCompaniesByAverageRating(companies);
        }
        else if (sortCriteria.equals(SortCriteria.POPULARITY)) {
            sortCompaniesByReservationCount(companies);
        }

        return toCompanyListDto(companies);
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

    private List<CompanyListDto> toCompanyListDto(List<Company> companies) {
        List<CompanyListDto> companyResponseDtoList = new ArrayList<>();

        for (Company company : companies) {

            // 리본 패키지만 추출
            List<RebornPackageListDto> productResponseDtoList = Optional.ofNullable(company.getProducts())
                    .orElse(Collections.emptyList()).stream()
                    .filter(product -> product.getProductType().equals(ProductType.REBORN_PACKAGE))
                    .map(product -> RebornPackageListDto.builder()
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

}
