package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.ChatRoom;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentResponseDto;
import com.rainbowbridge.reborn.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatContentService chatContentService;
    private final CompanyService companyService;

    @Transactional
    public ChatContentResponseDto enterChatRoom(String companyId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Company company = companyService.getCompany(companyId);

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUserAndCompany(user, company);

        if (chatRoomOptional.isEmpty()) {
            // 아직 생성된 채팅방이 없으면
            // 1. 채팅방 생성
            ChatRoom newChatRoom = new ChatRoom(user, company);
            // 2. 업체 최초 안내 채팅 추가
            chatContentService.addCompanyFirstChat(newChatRoom, company);
            // 3. 채팅방 저장
            chatRoomRepository.save(newChatRoom);

            return chatContentService.getChatContentListDto(newChatRoom, session);
        }
        else {
            // 이미 생성된 채팅방이 있으면
            ChatRoom chatRoom = chatRoomOptional.get();
            return chatContentService.getChatContentListDto(chatRoom, session);
        }
    }
}
