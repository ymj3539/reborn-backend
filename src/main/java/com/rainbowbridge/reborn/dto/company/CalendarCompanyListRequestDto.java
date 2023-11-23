package com.rainbowbridge.reborn.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarCompanyListRequestDto {

    @NotNull
    private LocalDate selectedDate; // 선택된 일자

    @NotNull
    private int selectedTime;       // 선택된 시간

    @NotNull
    private double userLatitude;    // 사용자 위치 위도

    @NotNull
    private double userLongitude;   // 사용자 위치 경도

}
