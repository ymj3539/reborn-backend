package com.rainbowbridge.reborn.dto.company;

import com.rainbowbridge.reborn.dto.bundle.BundleListDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {

    @ApiModelProperty(value = "업체명", example = "포포즈 경기 김포점")
    private String name;

    @ApiModelProperty(value = "업체 설명", example = "")
    private String intro;

    @ApiModelProperty(value = "업체 주소", example = "")
    private String address;

    @ApiModelProperty(value = "영업 시간", example = "")
    private String businessHours;

    @ApiModelProperty(value = "업체 전화번호", example = "")
    private String telNum;

    @ApiModelProperty(value = "안내사항", example = "")
    private String notification;

    @ApiModelProperty(value = "찜 여부", example = "")
    private boolean heartYN;

    @ApiModelProperty(value = "평점 평균", example = "4.68")
    private double averageRating;

    @ApiModelProperty(value = "리뷰 수", example = "323")
    private int reviewCount;

    @ApiModelProperty(value = "주요 후기", example = "마음이 많이 힘들고 아프지만...")
    private String mainReview;

    @ApiModelProperty(value = "업체 대표 이미지 경로", example = "http://146.56.104.45:8080/home/opc/reborn-backend/src/main/resources/images/example.png")
    private String imagePath;

    @ApiModelProperty(value = "업체 이미지 개수")
    private int companyImageCount;

    @ApiModelProperty(value = "업체 이미지 경로")
    private List<String> companyImagePaths;

    @ApiModelProperty(value = "리본 고정 패키지")
    private List<BundleListDto> rebornBundles;

    @ApiModelProperty(value = "업체 전용 패키지")
    private List<BundleListDto> companyBundles;

}
