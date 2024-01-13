package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.Bundle;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendListDto;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendRequestDto;
import com.rainbowbridge.reborn.repository.BundleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BundleService {

    private final BundleRepository bundleRepository;
    private final HeatService heatService;

    public List<BundleRecommendListDto> getRecommendationList(BundleRecommendRequestDto dto, int limit) {
        List<Bundle> bundles = bundleRepository.findAll();

        Comparator<Bundle> comparator = Comparator.comparing(Bundle::getId);

        boolean isAnyFilterSelected = dto.isNearYN() || dto.isMostSoldYN() || dto.isWellRatedYN() || dto.isCheapYN();

        if (!isAnyFilterSelected || dto.isNearYN()) {
            comparator = comparator.thenComparing(bundle -> bundle.getDistance(dto.getUserLatitude(), dto.getUserLongitude()));
        }
        if (!isAnyFilterSelected || dto.isMostSoldYN()) {
            comparator = comparator.thenComparing(Bundle::getSales);
        }
        if (!isAnyFilterSelected || dto.isWellRatedYN()) {
            comparator = comparator.thenComparing(Bundle::getAverageRating);
        }
        if (!isAnyFilterSelected || dto.isCheapYN()) {
            comparator = comparator.thenComparing(Bundle::getPrice);
        }

        bundles.sort(comparator);

        return bundles.stream()
                .limit(limit)
                .map(bundle -> BundleRecommendListDto.builder()
                        .imagePath(Utils.getImagePath(bundle.getName()))
                        .companyName(bundle.getCompany().getName())
                        .bundleId(bundle.getId())
                        .bundleName(bundle.getName())
                        .price(bundle.getPrice())
                        .heartYN(heatService.check(bundle.getCompany(), dto.getToken()))
                        .build())
                .collect(Collectors.toList());
    }

}
