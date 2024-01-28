package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/files", produces = "application/json; charset=utf8")
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity add(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(fileService.addFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드에 실패하였습니다.");
        }
    }

}
