package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.File;
import com.rainbowbridge.reborn.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    public String getFilePath(String fileName) {
        return fileDir + fileName;
    }

    public Long addFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // 원래 파일 이름 추출
        String originName = file.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String ext = originName.substring(originName.lastIndexOf("."));

        // uuid와 확장자 결합
        String newName = uuid + ext;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + newName;

        // 파일 엔티티 생성
        File fileEntity = File.builder()
                .name(newName)
                .originName(originName)
                .ext(ext)
                .size(file.getSize())
                .stat(true)
                .build();

        // 실제로 서버에 uuid를 파일명으로 저장
        file.transferTo(new java.io.File(getFilePath(newName)));

        // 데이터베이스에 파일 정보 저장
        File savedFile = fileRepository.save(fileEntity);

        return savedFile.getId();
    }

}
