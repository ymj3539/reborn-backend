package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.domain.Region;
import com.rainbowbridge.reborn.domain.SortCriteria;
import com.rainbowbridge.reborn.dto.company.CompanyListDto;
import com.rainbowbridge.reborn.dto.company.CompanyResponseDto;
import com.rainbowbridge.reborn.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/companies", produces = "application/json; charset=utf8")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    @ApiOperation(value = "업체 상세 조회")
    public CompanyResponseDto get(@PathVariable String companyId) {
        return companyService.getCompanyAndProducts(companyId);
    }

    @GetMapping("/{companyId}/available-times")
    @ApiOperation(value = "업체별 예약 가능 시간 조회")
    public List<String> getAvailableTimeList(
            @PathVariable String companyId,
            @ApiParam(value = "선택된 일자", example = "2023-10-11") @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        return companyService.getAvailableTimeList(companyId, selectedDate);
    }

    @GetMapping("/calendar")
    @ApiOperation(value = "바로예약 캘린더 업체 리스트 조회")
    public List<CompanyListDto> getCalendarCompanyList(
            @ApiParam(value = "선택된 일자", example = "2023-10-11") @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            @ApiParam(value = "선택된 시간", example = "10") @RequestParam @NotNull int selectedTime,
            @ApiParam(value = "사용자 위치 위도", example = "35.2388660") @RequestParam @NotNull double userLatitude,
            @ApiParam(value = "사용자 위치 경도", example = "129.222829") @RequestParam @NotNull double userLongitude) {
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
