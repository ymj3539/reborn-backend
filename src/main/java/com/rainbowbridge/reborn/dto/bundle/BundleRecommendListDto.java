package com.rainbowbridge.reborn.dto.bundle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BundleRecommendListDto {

    private String imagePath;

    private String companyName;

    private Long bundleId;

    private String bundleName;

    private int price;

}
