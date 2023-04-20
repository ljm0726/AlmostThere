package com.almostThere.domain.chatting.controller;

import com.almostThere.domain.chatting.dto.ChattingRequestDto;
import com.almostThere.domain.chatting.dto.ChattingResponseDto;
import com.almostThere.global.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ChattingController {

    @Autowired
    RedisTemplate redisTemplate;

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/receive/{meetingId}")
    // /send로 메시지를 반환합니다.
    @SendTo("/send/{meetingId}")
    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 SocketVO를 1) 인자값, 2) 반환값으로 사용합니다.
    public ChattingRequestDto sendChatting(@DestinationVariable String meetingId, ChattingRequestDto chattingRequestDto) {

        // vo에서 getter로 nickname을 가져옵니다.
        String userName = chattingRequestDto.getNickname();
        // vo에서 setter로 message를 가져옵니다.
        String content = chattingRequestDto.getMessage();

//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
//        ChattingDto chattingDto = new ChattingDto(meetingId, 0L, chattingRequestDto.getMessage(), now);

        // redis에 저장
        ListOperations<String, ChattingRequestDto> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(meetingId, chattingRequestDto);

        // 생성자로 반환값을 생성합니다.
        ChattingRequestDto result = new ChattingRequestDto(userName, content);

        // 반환
        return result;
    }

    @GetMapping("/api/chat/{meetingId}")
    public BaseResponse getChatting(@PathVariable Long meetingId) {
        System.out.println(meetingId);
        ListOperations<String, ChattingRequestDto> listOperations = redisTemplate.opsForList();

        String id = Long.toString(meetingId);

        List<ChattingRequestDto> result = listOperations.range(id, 0, listOperations.size(id));
        ChattingResponseDto chattingResponseDto = new ChattingResponseDto(id, result);

        return BaseResponse.success(chattingResponseDto);
    }
}
