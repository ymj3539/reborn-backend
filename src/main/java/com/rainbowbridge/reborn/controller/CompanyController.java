package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.company.CalendarCompanyListRequestDto;
import com.rainbowbridge.reborn.dto.company.CalendarCompanyListResponseDto;
import com.rainbowbridge.reborn.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/calendar")
    @ApiOperation(value = "바로예약 캘린더 업체 리스트 조회")
    public List<CalendarCompanyListResponseDto> getCalendarCompanyList(@RequestBody CalendarCompanyListRequestDto dto) {
        return companyService.getCalendarCompanyList(dto);
    }

}
