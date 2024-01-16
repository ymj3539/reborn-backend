package com.rainbowbridge.reborn.dto.bundle;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BundleRecommendRequestDto {

    @ApiModelProperty(value = "가까운 순 YN", example = "true")
    @NotNull
    private boolean nearYN;

    @ApiModelProperty(value = "구매 많은 YN", example = "true")
    @NotNull
    private boolean mostSoldYN;

    @ApiModelProperty(value = "평이 좋은 YN", example = "true")
    @NotNull
    private boolean wellRatedYN;

    @ApiModelProperty(value = "저렴한 YN", example = "true")
    @NotNull
    private boolean cheapYN;

    @ApiModelProperty(value = "사용자 위치 위도", example = "35.2388660")
    private double userLatitude;

    @ApiModelProperty(value = "사용자 위치 경도", example = "129.222829")
    private double userLongitude;

}
