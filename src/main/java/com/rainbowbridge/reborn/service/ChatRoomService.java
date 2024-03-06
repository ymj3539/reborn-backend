package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.domain.*;
import com.rainbowbridge.reborn.dto.chatRoom.ChatRoomDto;
import com.rainbowbridge.reborn.dto.chatRoom.ChatRoomListDto;
import com.rainbowbridge.reborn.dto.chatRoom.CompanyChatRoomDto;
import com.rainbowbridge.reborn.dto.chatRoom.CompanyChatRoomListDto;
import com.rainbowbridge.reborn.dto.reservation.CheckReservationResponseDto;
import com.rainbowbridge.reborn.dto.reservation.ReservationDto;
import com.rainbowbridge.reborn.repository.ChatRoomRepository;
import com.rainbowbridge.reborn.repository.CompanyRepository;
import com.rainbowbridge.reborn.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final CompanyRepository companyRepository;
    private final ChatContentService chatContentService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ChatRoomListDto> getChatRoomList(String token) {
        User user = userService.checkUser(token);

        return chatRoomRepository.findAllByUser(user).stream()
                .map(chatRoom -> {
                    Company company = chatRoom.getCompany();
                    boolean reservationYN = reservationService.checkReservation(company, token).isReservationYN();
                    List<ChatContent> chatContents = chatRoom.getChatContents();
                    String recentChat = chatContents.isEmpty() ? "" : chatContents.get(chatContents.size() - 1).getContent();

                    return ChatRoomListDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .companyId(company.getId())
                            .companyName(company.getName())
                            .companyImagePath(Utils.getImagePath(company.getNickname()))
                            .reservationYN(reservationYN)
                            .recentChat(recentChat)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CompanyChatRoomListDto> getChatRoomList(Long companyId) {
        return chatRoomRepository.findAllByCompanyId(companyId).stream()
                .map(chatRoom -> {
                    Company company = chatRoom.getCompany();
                    boolean reservationYN = reservationService.checkReservation(company, chatRoom.getUser()).isReservationYN();
                    List<ChatContent> chatContents = chatRoom.getChatContents();
                    String recentChat = chatContents.isEmpty() ? "" : chatContents.get(chatContents.size() - 1).getContent();

                    return CompanyChatRoomListDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .userId(chatRoom.getUser().getId())
                            .companyId(company.getId())
                            .userName(chatRoom.getUser().getName())
                            .reservationYN(reservationYN)
                            .recentChat(recentChat)
                            .recentChatTime(formatChatSendTime(chatContents.get(chatContents.size() - 1).getCreateDt()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ChatRoomDto getChatRoom(Long chatRoomId, String token){
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(chatRoomId);

        ChatRoom chatRoom = chatRoomOptional.get();

        Company company = chatRoom.getCompany();
        CheckReservationResponseDto reservation = reservationService.checkReservation(company, token);

        return ChatRoomDto.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .companyImagePath(Utils.getImagePath(company.getNickname()))
                .reservationYN(reservation.isReservationYN())
                .reservationDate(reservation.getReservationDate())
                .build();
    }

    public CompanyChatRoomDto getCompanyChatRoom(Long chatRoomId){
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(chatRoomId);

        ChatRoom chatRoom = chatRoomOptional.get();

        Company company = chatRoom.getCompany();
        User user = chatRoom.getUser();
        ReservationDto reservationDto = reservationService.findRecentReservationDto(company, user);

        return CompanyChatRoomDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .companyId(company.getId())
                .reservationYN(reservationDto.isReservationYN())
                .reservationId(reservationDto.getReservationId())
                .reservationDate(reservationDto.getReservationDate())
                .bundleName(reservationDto.getBundleName())
                .build();
    }

    @Transactional
    public ChatRoomDto enterChatRoom(Long companyId, String token) {
        User user = userService.checkUser(token);
        Company company =companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 업체입니다."));

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUserAndCompany(user, company);

        if (chatRoomOptional.isEmpty()) {
            // 아직 생성된 채팅방이 없으면
            // 1. 채팅방 생성
            ChatRoom newChatRoom = ChatRoom.builder().user(user).company(company).build();
            // 2. 업체 최초 안내 채팅 추가
            chatContentService.addCompanyFirstChat(newChatRoom, company);
            // 3. 채팅방 저장
            chatRoomRepository.save(newChatRoom);

            return getChatRoom(newChatRoom.getId(), token);
        }
        else {
            // 이미 생성된 채팅방이 있으면
            ChatRoom chatRoom = chatRoomOptional.get();
            return getChatRoom(chatRoom.getId(), token);
        }
    }

    private String formatChatSendTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
        return dateTime.format(formatter);
    }
}
