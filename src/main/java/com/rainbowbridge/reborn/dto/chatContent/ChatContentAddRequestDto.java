package com.rainbowbridge.reborn.dto.chatContent;

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
public class ChatContentAddRequestDto {

    @NotBlank
    private Long chatRoomId;

    @NotNull
    private String content;

}
