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

        ChatRoom chatRoom = new ChatRoom();

        if (chatRoomRepository.findAllByUserAndCompany(user, company).size() == 0) {
            // 아직 생성된 채팅방이 없으면
            // 1. 채팅방 생성
            chatRoom = new ChatRoom(user, company);
            // 2. 업체 최초 안내 채팅 추가
            chatContentService.addCompanyFirstChat(chatRoom, company);
            // 3. 채팅방 저장
            chatRoomRepository.save(chatRoom);
        }

        // 채팅 목록 조회
        return chatContentService.getChatContentListDto(chatRoom, session);
    }
}
