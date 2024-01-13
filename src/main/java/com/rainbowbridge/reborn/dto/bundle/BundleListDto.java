package com.rainbowbridge.reborn.dto.bundle;

import com.rainbowbridge.reborn.domain.Bundle;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BundleListDto {

    @ApiModelProperty(value = "번들ID")
    private Long id;

    @ApiModelProperty(value = "번들 명", example = "BASIC")
    private String name;

    @ApiModelProperty(value = "가격", example = "500000")
    private int price;

    @ApiModelProperty(value = "번들 설명", example = "염습+개별화장+기본관+꽃장식")
    private String intro;

    @ApiModelProperty(value = "번들 이미지 경로", example = "http://146.56.104.45:8080/home/opc/reborn-backend/src/main/resources/images/example.png")
    private String imagePath;

    public BundleListDto(Bundle bundle, String intro, String imagePath) {
        this.id = bundle.getId();
        this.name = bundle.getName();
        this.price = bundle.getPrice();
        this.intro = intro;
        this.imagePath = imagePath;
    }

}
