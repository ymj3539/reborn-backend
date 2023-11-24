package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.domain.Region;
import com.rainbowbridge.reborn.domain.SortCriteria;
import com.rainbowbridge.reborn.dto.company.CompanyListDto;
import com.rainbowbridge.reborn.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/calendar")
    @ApiOperation(value = "바로예약 캘린더 업체 리스트 조회")
    public List<CompanyListDto> getCalendarCompanyList(
            @RequestParam @NotNull LocalDate selectedDate,
            @RequestParam @NotNull int selectedTime,
            @RequestParam @NotNull double userLatitude,
            @RequestParam @NotNull double userLongitude) {
        return companyService.getCalendarCompanyList(selectedDate, selectedTime, userLatitude, userLongitude);
    }

    @GetMapping("/filter")
    @ApiOperation(value = "정렬순과 지역권에 따른 업체 리스트 조회")
    public List<CompanyListDto> getFilteredCompanyList(
            @RequestParam @NotNull SortCriteria sortCriteria,
            @RequestParam @NotNull Region region) {
        // 매개변수들을 사용하여 로직을 수행
        return companyService.getFilteredCompanyList(sortCriteria, region);
    }

}
