package com.rainbowbridge.reborn.dto.heart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeartListDto {

    private String id;

    private String name;

    private String address;

    private String imagePath;

}
