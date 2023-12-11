package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.ChatContent;
import com.rainbowbridge.reborn.domain.ChatRoom;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentResponseDto;
import com.rainbowbridge.reborn.dto.chatRoom.ChatRoomListDto;
import com.rainbowbridge.reborn.dto.reservation.CheckReservationResponseDto;
import com.rainbowbridge.reborn.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatContentService chatContentService;
    private final CompanyService companyService;
    private final ReservationService reservationService;

    public List<ChatRoomListDto> getChatRoomList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        return chatRoomRepository.findAllByUser(user).stream()
                .map(chatRoom -> {
                    Company company = chatRoom.getCompany();
                    boolean reservationYN = reservationService.checkReservation(company, session).isReservationYN();
                    List<ChatContent> chatContents = chatRoom.getChatContents();
                    String recentChat = chatContents.isEmpty() ? "" : chatContents.get(chatContents.size() - 1).getContent();

                    return ChatRoomListDto.builder()
                            .companyName(company.getName())
                            .companyImagePath(Utils.getImagePath(company.getName()))
                            .reservationYN(reservationYN)
                            .recentChat(recentChat)
                            .build();
                })
                .collect(Collectors.toList());
    }

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
