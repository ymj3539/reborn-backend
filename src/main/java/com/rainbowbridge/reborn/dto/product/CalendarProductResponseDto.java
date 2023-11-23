package com.rainbowbridge.reborn.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarProductResponseDto {

    private Long id;

    private String name;

    private int price;

}
