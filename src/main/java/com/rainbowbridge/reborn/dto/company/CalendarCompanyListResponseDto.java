package com.rainbowbridge.reborn.dto.company;

import com.rainbowbridge.reborn.dto.product.CalendarProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarCompanyListResponseDto {

    private String id;

    private String name;

    private List<CalendarProductResponseDto> products;

}
