package com.rainbowbridge.reborn.dto.product;

import com.rainbowbridge.reborn.domain.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageListDto {

    @ApiModelProperty(value = "상품ID")
    private Long id;

    @ApiModelProperty(value = "상품명", example = "BASIC")
    private String name;

    @ApiModelProperty(value = "가격", example = "포포즈 경기 김포점")
    private int price;

    @ApiModelProperty(value = "상품 설명", example = "염습+개별화장+기본관+꽃장식")
    private String intro;

    @ApiModelProperty(value = "상품 이미지 경로", example = "http://146.56.104.45:8080/home/opc/reborn-backend/src/main/resources/images/example.png")
    private String imagePath;

    public PackageListDto(Product product, String imagePath) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.intro = product.getIntro();
        this.imagePath = imagePath;
    }
}
