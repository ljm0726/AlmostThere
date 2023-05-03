package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.chatting.entity.Chatting;
import lombok.Getter;

@Getter
public class ChattingDetailDto extends ChattingDto {

    // 모임 코드
    private String roomCode;

    public ChattingDetailDto(ChattingDto chattingDto, String roomCode) {
        super(chattingDto.getMemberId(), chattingDto.getMessage(), chattingDto.getChattingTime());
        this.roomCode = roomCode;
    }

    public ChattingDetailDto(Chatting chatting) {
        super(chatting.getMember().getId(), chatting.getMessage(), chatting.getChattingTime());
        this.roomCode = chatting.getMeeting().getRoomCode();
    }
}
