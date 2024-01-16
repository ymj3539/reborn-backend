package com.rainbowbridge.reborn.controller;

import com.google.common.net.HttpHeaders;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendListDto;
import com.rainbowbridge.reborn.dto.bundle.BundleRecommendRequestDto;
import com.rainbowbridge.reborn.dto.bundle.BundleResponseDto;
import com.rainbowbridge.reborn.service.BundleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/bundles", produces = "application/json; charset=utf8")
public class BundleController {

    private final BundleService bundleService;

    @GetMapping("/{bundleId}")
    @ApiOperation(value = "번들 상세 조회")
    public BundleResponseDto get(@PathVariable Long bundleId) {
        return bundleService.getBundle(bundleId);
    }

    @GetMapping("/recommendation")
    @ApiOperation(value = "장례 번들 추천")
    public List<BundleRecommendListDto> getRecommendedProductList(@RequestBody BundleRecommendRequestDto dto) {
         return bundleService.getRecommendationList(dto, 3);
    }

    @GetMapping("/recommendation/more")
    @ApiOperation(value = "장례 번들 추천 더보기")
    public List<BundleRecommendListDto> getMoreRecommendedProductList(@RequestBody BundleRecommendRequestDto dto) {
        return bundleService.getRecommendationList(dto, 10);
    }

}
