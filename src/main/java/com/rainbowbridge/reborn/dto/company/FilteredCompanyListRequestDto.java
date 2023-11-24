package com.rainbowbridge.reborn.dto.company;

import com.rainbowbridge.reborn.domain.Region;
import com.rainbowbridge.reborn.domain.SortCriteria;
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
public class FilteredCompanyListRequestDto {

    @NotNull
    @ApiModelProperty(value = "정렬 기준", allowableValues = "RATING, POPULARITY")
    private SortCriteria sortCriteria;

    @NotNull
    @ApiModelProperty(value = "지역권", allowableValues = "ALL, CAPITAL, CHUNGCHEONG, GANGWON, YEONGNAM, HONAM")
    private Region region;


}
