package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.bundle.BundleRecommendListDto;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendRequestDto;
import com.rainbowbridge.reborn.service.BundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/bundles", produces = "application/json; charset=utf8")
public class BundleController {

    private final BundleService bundleService;

    @GetMapping("/recommendation")
    public List<BundleRecommendListDto> getRecommendedProductList(@RequestBody BundleRecommendRequestDto dto) {
         return bundleService.getRecommendationList(dto, 3);
    }

    @GetMapping("/recommendation/more")
    public List<BundleRecommendListDto> getMoreRecommendedProductList(@RequestBody BundleRecommendRequestDto dto) {
        return bundleService.getRecommendationList(dto, 10);
    }

}
