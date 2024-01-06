package com.rainbowbridge.reborn.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapCompanyListDto {

    private int key;

    private Long id;

    private String name;

    private String imagePath;

    private double latitude;

    private double longitude;

}
