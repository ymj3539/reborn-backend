package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.product.RecommendedProductListDto;
import com.rainbowbridge.reborn.service.ProductService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/recommendation")
    public List<RecommendedProductListDto> getRecommendedProductList(
            @ApiParam(value = "사용자 위치 위도", example = "35.2388660") @RequestParam(required = false, defaultValue = "0.0") double userLatitude,
            @ApiParam(value = "사용자 위치 경도", example = "129.222829") @RequestParam(required = false, defaultValue = "0.0") double userLongitude) {
        if (userLatitude != 0.0 && userLongitude != 0.0) {
            return productService.getRecommendedProductList(userLatitude, userLongitude);
        }
        else {
            return productService.getGeneralRecommendedProductList();
        }
    }

}
