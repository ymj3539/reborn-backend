package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.dto.heart.HeartListDto;
import com.rainbowbridge.reborn.service.HeatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/hearts", produces = "application/json; charset=utf8")
public class HeartController {

    private final HeatService heatService;

    @PostMapping("/{companyId}")
    @ApiOperation(value = "찜 추가/삭제")
    public ResponseEntity toggle(@PathVariable Long companyId, @RequestParam(required = false, defaultValue = "") String userId) {
        if (heatService.toggleHeart(companyId, userId)) {
            // 찜이 되어 있지 않으면 추가
            return Utils.createResponse("찜 추가에 성공했습니다.", HttpStatus.OK);
        }
        else {
            // 이미 찜이 되어 있으면 삭제
            return Utils.createResponse("찜 삭제에 성공했습니다.", HttpStatus.OK);
        }
    }

    @GetMapping
    @ApiOperation(value = "찜한 업체 목록 조회")
    public List<HeartListDto> add(@RequestParam(required = false, defaultValue = "") String userId) {
        return heatService.getHeartList(userId);
    }

}
