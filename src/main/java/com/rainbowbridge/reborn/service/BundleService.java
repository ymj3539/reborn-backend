package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.Bundle;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendListDto;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendRequestDto;
import com.rainbowbridge.reborn.dto.bundle.BundleResponseDto;
import com.rainbowbridge.reborn.dto.product.ProductListDto;
import com.rainbowbridge.reborn.dto.supplement.SupplementListDto;
import com.rainbowbridge.reborn.repository.BundleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BundleService {

    private final BundleRepository bundleRepository;
    private final HeatService heatService;

    public BundleResponseDto getBundle(Long bundleId) {
        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 번들입니다."));

        List<ProductListDto> products = bundle.getProducts().stream()
                .map(product -> ProductListDto.builder()
                        .imagePath(Utils.getImagePath(product.getName()))
                        .name(product.getName())
                        .build())
                .collect(Collectors.toList());

        List<SupplementListDto> supplements = bundle.getCompany().getSupplements().stream()
                .map(supplement -> SupplementListDto.builder()
                        .id(supplement.getId())
                        .name(supplement.getName())
                        .price(supplement.getPrice())
                        .intro(supplement.getIntro())
                        .imagePath(Utils.getImagePath(supplement.getName()))
                        .build())
                .collect(Collectors.toList());

        return BundleResponseDto.builder()
                .imagePath(Utils.getImagePath(bundle.getName()))
                .companyName(bundle.getCompany().getName())
                .bundleName(bundle.getName())
                .price(bundle.getPrice())
                .products(products)
                .companySupplements(supplements)
                .build();
    }

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
                        .build())
                .collect(Collectors.toList());
    }

}
