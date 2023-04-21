package com.almostThere.domain.chatting.controller;

import com.almostThere.domain.chatting.dto.ChattingDto;
import com.almostThere.domain.chatting.dto.ChattingMemberDto;
import com.almostThere.domain.chatting.service.ChattingService;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    /**
     * jeey0124
     * @param meetingId 미팅ID
     * @param message 채팅 내용
     * @return 보낸 메시지 정보를 반환한다.
     * **/
    @MessageMapping("/receive/{meetingId}") // 메시지를 받을 endpoint 설정
    @SendTo("/send/{meetingId}") // 메시지를 보낼 곳 설정
    public BaseResponse sendChatting(@DestinationVariable Long meetingId, String message) {

        // 사용자ID 임시값
        Long memberId = 1L;
        
        // 해당 채팅방의 멤버가 맞는지 확인
        
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // redis에 저장
        ChattingDto chattingDto = chattingService.addChattingRedis(memberId, meetingId, message, now);

        // 반환
        return BaseResponse.success(chattingDto);
    }

    /**
     * jeey0124
     * @param meetingId 미팅ID
     * @return 채팅 정보 및 채팅 기록 30개를 조회한다.
     * **/
    @GetMapping("/api/chat/{meetingId}")
    public BaseResponse getChattingAll(@PathVariable Long meetingId) {

        // 사용자ID 임시값
        Long memberId = 1L;

        // 해당 채팅방의 멤버가 맞는지 확인
        
        // 채팅 관련 정보 가져오기
        
        // 채팅 기록 전부 가져오기
        Map<Long, ChattingMemberDto> chattingMemberMap = chattingService.getChatting30(meetingId);
        
        return BaseResponse.fail();
    }
}
