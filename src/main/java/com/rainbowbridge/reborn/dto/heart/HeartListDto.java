package com.rainbowbridge.reborn.dto.heart;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeartListDto {

    private Long id;

    private String name;

    private String address;

    private String businessHours;

    private String imagePath;

}
