package com.rainbowbridge.reborn.dto.supplement;

import com.rainbowbridge.reborn.domain.Supplement;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementListDto {

    @ApiModelProperty(value = "옵션ID")
    private Long id;

    @ApiModelProperty(value = "옵션 명")
    private String name;

    @ApiModelProperty(value = "가격")
    private int price;

    @ApiModelProperty(value = "옵션 설명")
    private String intro;

    @ApiModelProperty(value = "옵션 이미지 경로", example = "http://146.56.104.45:8080/home/opc/reborn-backend/src/main/resources/images/example.png")
    private String imagePath;

    public SupplementListDto(Supplement supplement, String imagePath) {
        this.id = supplement.getId();
        this.name = supplement.getName();
        this.price = supplement.getPrice();
        this.intro = supplement.getIntro();
        this.imagePath = imagePath;
    }

}
