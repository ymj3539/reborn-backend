package com.rainbowbridge.reborn.dto.product;

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

}
