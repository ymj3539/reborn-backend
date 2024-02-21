package com.rainbowbridge.reborn.dto.chatContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private String content;
    private Date createdAt;
    private long chatRoomId;
    private String authorId;

    @Override
    public String toString() {
        return "ChatMessageDto{" +
                "content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", chatRoomId=" + chatRoomId +
                ", authorId='" + authorId + '\'' +
                '}';
    }
}
