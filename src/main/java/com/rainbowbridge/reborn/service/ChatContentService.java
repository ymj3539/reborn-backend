package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.ChatContent;
import com.rainbowbridge.reborn.domain.ChatRoom;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentAddRequestDto;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentListDto;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentResponseDto;
import com.rainbowbridge.reborn.dto.reservation.CheckReservationResponseDto;
import com.rainbowbridge.reborn.repository.ChatContentRepository;
import com.rainbowbridge.reborn.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatContentService {

    private final ChatContentRepository chatContentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReservationService reservationService;
    private final UserService userService;

    public void addUserChat(ChatContentAddRequestDto dto, String userId) {
        User user = userService.checkUser(userId);

        if (user == null) {
            throw new EntityNotFoundException("사용자 로그인 정보가 없습니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

        chatContentRepository.save(ChatContent.builder()
                                                .content(dto.getContent())
                                                .chatRoom(chatRoom)
                                                .user(user)
                                                .build());
    }

    public void addCompanyFirstChat(ChatRoom chatRoom, Company company) {
        String content = "안녕하세요. ‘" + company.getName() + "’ 상담원입니다. 어떤 도움이 필요하신가요?";

        chatContentRepository.save(ChatContent.builder()
                .content(content)
                .chatRoom(chatRoom)
                .company(company)
                .build());
    }

    @Transactional(readOnly = true)
    public ChatContentResponseDto getChatContentListDto(ChatRoom chatRoom, String userId) {
        List<ChatContentListDto> chatContents = chatContentRepository.findAllByChatRoom(chatRoom).stream()
                .map(chatContent -> ChatContentListDto.builder()
                        .userSendYN(checkUserSendYn(chatContent))
                        .content(chatContent.getContent())
                        .sendDate(formatChatSendDate(chatContent.getCreateDt()))
                        .sendTime(formatChatSendTime(chatContent.getCreateDt()))
                        .build())
                .collect(Collectors.toList());

        Company company = chatRoom.getCompany();
        CheckReservationResponseDto reservation = reservationService.checkReservation(company, userId);

        return ChatContentResponseDto.builder()
                .companyName(company.getName())
                .companyImagePath(Utils.getImagePath(company.getNickname()))
                .reservationYN(reservation.isReservationYN())
                .reservationDate(reservation.getReservationDate())
                .chatContentListDtos(chatContents)
                .build();
    }

    private boolean checkUserSendYn(ChatContent chatContent) {
        User user = chatContent.getUser();
        Company company = chatContent.getCompany();

        return user != null && company == null;
    }

    private String formatChatSendDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 E요일");
        return dateTime.format(formatter);
    }

    private String formatChatSendTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
        return dateTime.format(formatter);
    }

}
