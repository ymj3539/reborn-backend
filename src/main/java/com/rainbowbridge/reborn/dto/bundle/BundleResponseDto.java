package com.rainbowbridge.reborn.dto.bundle;

import com.rainbowbridge.reborn.dto.product.ProductListDto;
import com.rainbowbridge.reborn.dto.supplement.SupplementListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BundleResponseDto {

    private String imagePath;

    private String companyName;

    private String bundleName;

    private int price;

    private List<ProductListDto> products;

    private List<SupplementListDto> heartSupplements;

    private List<SupplementListDto> companySupplements;


}
