package com.rainbowbridge.reborn.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayResponseDto {

    private int day;

    private String dayOfWeek;

}
