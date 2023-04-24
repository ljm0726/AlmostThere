package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.chatting.entity.Chatting;
import lombok.Getter;

@Getter
public class ChattingDetailDto extends ChattingDto {

    // 사용자가 입력한 채팅인지
    private boolean isMine;

    public ChattingDetailDto(ChattingDto chattingDto, Long memberId) {
        super(chattingDto.getMeetingId(), chattingDto.getMemberId(), chattingDto.getMessage(), chattingDto.getChattingTime());

        if (memberId == chattingDto.getMemberId()) isMine = true;
        else isMine = false;
    }

    public ChattingDetailDto(Chatting chatting, Long memberId) {
        super(chatting.getMeeting().getId(), chatting.getMember().getId(), chatting.getMessage(), chatting.getChattingTime());

        if (memberId == chatting.getMember().getId()) isMine = true;
        else isMine = false;
    }
}
