package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.ProductType;
import com.rainbowbridge.reborn.dto.product.RecommendedProductListDto;
import com.rainbowbridge.reborn.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;;import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyService companyService;

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
    }

    public List<RecommendedProductListDto> getNearbyRecommendedProductList(double userLatitude, double userLongitude) {
        List<Company> allCompanies = companyService.getCompanyList();
        List<Company> nearCompanies = companyService.sortCompanyListByDistance(allCompanies, userLatitude, userLongitude).subList(0, 15);
        return selectProducts(nearCompanies);
    }

    public List<RecommendedProductListDto> getAllRecommendedProductList() {
        List<Company> allCompanies = companyService.getCompanyList();
        return selectProducts(allCompanies);
    }

    private List<RecommendedProductListDto> selectProducts(List<Company> companies) {
        List<Company> popularCompanies = companyService.sortCompaniesByReservationCount(companies).subList(0, 10);
        List<Company> goodCompanies = companyService.sortCompaniesByAverageRating(popularCompanies).subList(0, 5);

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
                        .imagePath(Utils.getImagePath(product.getName()))
                        .companyId(product.getCompany().getId())
                        .companyName(product.getCompany().getName())
                        .productName(product.getName())
                        .price(product.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

}
