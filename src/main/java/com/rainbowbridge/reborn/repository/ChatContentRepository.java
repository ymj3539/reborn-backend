package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.ChatContent;
import com.rainbowbridge.reborn.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatContentRepository extends JpaRepository<ChatContent, Long> {

    List<ChatContent> findAllByChatRoom(ChatRoom chatRoom);

}
