package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.dto.common.DayResponseDto;
import com.rainbowbridge.reborn.dto.company.CalendarCompanyListRequestDto;
import com.rainbowbridge.reborn.dto.company.CalendarCompanyListResponseDto;
import com.rainbowbridge.reborn.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CompanyService companyService;

    @GetMapping
    @ApiOperation(value = "캘린더 일자 조회")
    public List<DayResponseDto> getDayList() {
        List<DayResponseDto> dtos = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            int day = date.getDayOfMonth();
            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);

            dtos.add(new DayResponseDto(day, dayOfWeek));
        }

        return dtos;
    }

    @GetMapping("/companies")
    @ApiOperation(value = "바로예약 캘린더 업체 리스트 조회")
    public List<CalendarCompanyListResponseDto> getCalendarCompanyList(@RequestBody CalendarCompanyListRequestDto dto) {
        return companyService.getCalendarCompanyList(dto);
    }


}
