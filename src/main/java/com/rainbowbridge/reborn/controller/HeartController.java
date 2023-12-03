package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.service.HeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hearts")
public class HeartController {

    private final HeatService heatService;

    @PostMapping("/{companyId}")
    public ResponseEntity add(@PathVariable String companyId) {
       heatService.addHeart(companyId);
       return ResponseEntity.ok("찜하기에 성공했습니다");
    }

}
