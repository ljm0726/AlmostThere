package com.almostThere.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ChattingDto implements Serializable {

    // 채팅 입력한 멤버 ID
    private Long memberId;
    
    // 채팅 내용
    private String message;
    
    // 채팅 입력 일시
    private LocalDateTime chattingTime;
}
