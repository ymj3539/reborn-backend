package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.service.HeatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hearts")
public class HeartController {

    private final HeatService heatService;

    @PostMapping("/{companyId}")
    @ApiOperation(value = "찜 하기")
    public ResponseEntity add(@PathVariable String companyId, HttpSession session) {
       heatService.addHeart(companyId, session);
       return ResponseEntity.ok("찜 하기에 성공했습니다");
    }

}
