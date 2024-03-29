package com.rainbowbridge.reborn.dto.company;

import com.rainbowbridge.reborn.dto.bundle.RebornBundleListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListDto {

    private Long id;

    private String name;

    private String imagePath;

    private List<RebornBundleListDto> bundles;

}
