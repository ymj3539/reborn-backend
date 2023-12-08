package com.rainbowbridge.reborn.dto.pet;

import com.rainbowbridge.reborn.domain.Pet;
import com.rainbowbridge.reborn.domain.Species;
import com.rainbowbridge.reborn.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetAddRequestDto {

    @NotNull
    @ApiModelProperty(value = "구분", example = "DOG")
    private Species species;

    @NotBlank
    @ApiModelProperty(value = "종", example = "말티즈")
    private String breed;

    @NotBlank
    @ApiModelProperty(value = "이름", example = "복실이")
    private String name;

    @NotNull
    @ApiModelProperty(value = "나이 - 년", example = "9")
    private int years;

    @NotNull
    @ApiModelProperty(value = "나이 - 개월", example = "10")
    private int months;

    @NotNull
    @ApiModelProperty(value = "체중", example = "5.7")
    private double weight;

    public Pet toEntity(User user) {
        return Pet.builder()
                .species(species)
                .breed(breed)
                .name(name)
                .years(years)
                .months(months)
                .weight(weight)
                .user(user)
                .build();
    }

}
