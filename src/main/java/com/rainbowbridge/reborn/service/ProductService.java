package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.ProductType;
import com.rainbowbridge.reborn.dto.product.RecommendedProductListDto;
import com.rainbowbridge.reborn.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;;import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CompanyService companyService;
    private final CommonService commonService;

    public List<RecommendedProductListDto> getRecommendedProductList(double userLatitude, double userLongitude) {
        // 모든 업체 조회
        List<Company> allCompanies = companyService.getCompanyList();

        // 가까운 순 15개 업체 조회
        List<Company> nearCompanies = companyService.sortCompanyListByDistance(allCompanies, userLatitude, userLongitude).subList(0, 15);

        // 구매 많은 순 10개 업체 조회
        List<Company> popularCompanies = companyService.sortCompaniesByReservationCount(nearCompanies).subList(0, 10);

        // 평이 좋은 순 5개 업체 조회
        List<Company> goodCompanies = companyService.sortCompaniesByAverageRating(popularCompanies).subList(0, 5);

        // 저렴한 순 3개 패키지 상품 조회
        EnumSet<ProductType> targetTypes = EnumSet.of(ProductType.REBORN_PACKAGE, ProductType.COMPANY_PACKAGE);
        List<Product> cheapProducts = goodCompanies.stream()
                .flatMap(company -> company.getProducts().stream())
                .filter(product -> targetTypes.contains(product.getProductType()))
                .sorted(Comparator.comparing(Product::getPrice))
                .limit(3)
                .collect(Collectors.toList());

        // DTO 생성
        return cheapProducts.stream()
                .map(product -> RecommendedProductListDto.builder()
                        .imagePath(commonService.getImagePath(product.getName()))
                        .companyName(product.getCompany().getName())
                        .productName(product.getName())
                        .price(product.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

}
