package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.common.DayResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/common", produces = "application/json; charset=utf8")
public class CommonController {

    @GetMapping("/calendar")
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

}
