package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.ChatContent;
import com.rainbowbridge.reborn.domain.ChatRoom;
import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.repository.ChatContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatContentService {

    private final ChatContentRepository chatContentRepository;

    public void addCompanyFirstChat(ChatRoom chatRoom, Company company) {
        String content = "안녕하세요. ‘" + company.getName() + "’ 상담원입니다. 어떤 도움이 필요하신가요?";

        chatContentRepository.save(new ChatContent(content, chatRoom, company));
    }

}
