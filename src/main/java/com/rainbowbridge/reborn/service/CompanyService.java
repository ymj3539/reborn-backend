package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.BundleType;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.Region;
import com.rainbowbridge.reborn.domain.SortCriteria;
import com.rainbowbridge.reborn.domain.TimeOff;
import com.rainbowbridge.reborn.dto.bundle.BundleListDto;
import com.rainbowbridge.reborn.dto.bundle.RebornBundleListDto;
import com.rainbowbridge.reborn.dto.company.CompanyListDto;
import com.rainbowbridge.reborn.dto.company.CompanyResponseDto;
import com.rainbowbridge.reborn.dto.company.MapCompanyListDto;
import com.rainbowbridge.reborn.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HeatService heatService;

    @Transactional(readOnly = true)
    public List<Company> getCompanyList() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompany(Long companyId, String token) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 업체입니다."));

        double averageRating = company.getAverageRating();

        int reviewCount = company.getReservations().size();

        String mainReview = company.getReviews().isEmpty() ? null : company.getReviews().get(company.getReviews().size() - 1).getContent();

        List<String> companyImagePaths = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            companyImagePaths.add(Utils.getImagePath("COMPANY" + i));
        }

        return CompanyResponseDto.builder()
                .name(company.getName())
                .intro(company.getIntro())
                .address(company.getAddress())
                .businessHours(Utils.convertTimeRangeFormat(company.getOpenTime(), company.getCloseTime()))
                .telNum(company.getTelNum())
                .notification(company.getNotification())
                .heartYN(heatService.check(company, token))
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .mainReview(mainReview)
                .imagePath(Utils.getImagePath(company.getNickname()))
                .companyImageCount(4)
                .companyImagePaths(companyImagePaths)
                .rebornBundles(getBundlesByType(company, BundleType.REBORN_BUNDLE))
                .companyBundles(getBundlesByType(company, BundleType.COMPANY_BUNDLE))
                .build();
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableTimeList(Long companyId, LocalDate selectedDate) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 업체입니다."));

        int openTime = company.getOpenTime();
        int closeTime = company.getCloseTime();

        List<Integer> timesOffs = company.getTimeOffs().stream()
                .filter(timeOff -> timeOff.getDate().equals(selectedDate))
                .map(TimeOff::getTime)
                .collect(Collectors.toList());

        List<String> availableTimeList = new ArrayList<>();
        for (int i = openTime; i <= closeTime; i++) {
            if (!timesOffs.contains(i)) {
                String timeString = Utils.convertTimeFormat(i);
                availableTimeList.add(timeString);
            }
        }

        return availableTimeList;
    }

    @Transactional(readOnly = true)
    public List<CompanyListDto> getCalendarCompanyList(LocalDate selectedDate, int selectedTime, double userLatitude, double userLongitude) {
        // 영업 가능한 업체 조회
        List<Company> availableCompanies = companyRepository.findAvailableCompanieList(selectedDate, selectedTime);
        // 가까운 순으로 업체 정렬
        List<Company> sortedCompanies = sortCompanyListByDistance(availableCompanies, userLatitude, userLongitude);
        // 가장 가까운 10개 업체만 선택.
        List<Company> nearbyAvailableCompanies = sortedCompanies.size() > 10 ? sortedCompanies.subList(0, 10) : sortedCompanies;

        return toCompanyListDto(nearbyAvailableCompanies);
    }

    @Transactional(readOnly = true)
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
            companies = sortCompaniesByAverageRating(companies);
        }
        else if (sortCriteria.equals(SortCriteria.POPULARITY)) {
            companies = sortCompaniesByReservationCount(companies);
        }

        return toCompanyListDto(companies);
    }

    @Transactional(readOnly = true)
    public List<MapCompanyListDto> getMapCompanyList(double userLatitude, double userLongitude, int radius) {
        AtomicInteger key = new AtomicInteger(1);

        return getCompanyList().stream()
                .filter(company -> Utils.calculateDistance(userLatitude, userLongitude, company.getLatitude(), company.getLongitude()) <= radius * 1000)
                .map(company -> MapCompanyListDto.builder()
                        .key(key.getAndIncrement())
                        .id(company.getId())
                        .name(company.getName())
                        .imagePath(Utils.getImagePath(company.getNickname()))
                        .latitude(company.getLatitude())
                        .longitude(company.getLongitude())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Company> sortCompanyListByDistance(List<Company> companies, double userLatitude, double userLongitude) {
        // 각 업체의 사용자의 거리를 계산하고, 그 결과를 Map에 저장
        Map<Company, Double> companyDistanceMap = new HashMap<>();
        for (Company company : companies) {
            double distance = Utils.calculateDistance(userLatitude, userLongitude, company.getLatitude(), company.getLongitude());
            companyDistanceMap.put(company, distance);
        }

        // Map의 값을 기준으로 가장 가까운 순으로 정렬
        List<Map.Entry<Company, Double>> entries = new ArrayList<>(companyDistanceMap.entrySet());
        entries.sort(Map.Entry.comparingByValue());

        return entries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Company> sortCompaniesByAverageRating(List<Company> companies) {
        companies.sort((c1, c2) -> {
            double avgRating1 = c1.getAverageRating();
            double avgRating2 = c2.getAverageRating();
            return Double.compare(avgRating2, avgRating1);  // 내림차순 정렬
        });

        return companies;
    }

    public List<Company> sortCompaniesByReservationCount(List<Company> companies) {
        companies.sort((c1, c2) -> {
            int count1 = c1.getReservations().size();
            int count2 = c2.getReservations().size();
            return Integer.compare(count2, count1);
        });

        return companies;
    }

    private List<CompanyListDto> toCompanyListDto(List<Company> companies) {
        List<CompanyListDto> companyResponseDtoList = new ArrayList<>();

        for (Company company : companies) {

            // 리본 패키지만 추출
            List<RebornBundleListDto> rebornBundles = Optional.ofNullable(company.getBundles())
                    .orElse(Collections.emptyList()).stream()
                    .filter(bundle -> bundle.getBundleType().equals(BundleType.REBORN_BUNDLE))
                    .map(bundle -> RebornBundleListDto.builder()
                            .id(bundle.getId())
                            .name(bundle.getName())
                            .price(bundle.getPrice())
                            .imagePath(Utils.getImagePath(bundle.getName()))
                            .build())
                    .collect(Collectors.toList());

            CompanyListDto companyListResponseDto = CompanyListDto.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .imagePath(Utils.getImagePath(company.getNickname()))
                    .bundles(rebornBundles)
                    .build();

            companyResponseDtoList.add(companyListResponseDto);
        }

        return companyResponseDtoList;
    }

    private List<BundleListDto> getBundlesByType(Company company, BundleType bundleType) {
        return Optional.ofNullable(company.getBundles())
                .orElse(Collections.emptyList()).stream()
                .filter(bundle -> bundle.getBundleType().equals(bundleType))
                .map(bundle -> {
                    String intro = bundle.getProducts().stream()
                            .map(Product::getName)
                            .collect(Collectors.joining(" + "));
                    return new BundleListDto(bundle, intro, Utils.getImagePath(bundle.getName()));
                })
                .collect(Collectors.toList());
    }

}
