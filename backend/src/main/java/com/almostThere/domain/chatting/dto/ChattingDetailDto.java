package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.chatting.entity.Chatting;
import lombok.Getter;

@Getter
public class ChattingDetailDto extends ChattingDto {

    // 모임 ID
    private Long meetingId;

    // 사용자가 입력한 채팅인지
    private boolean isMine;

    public ChattingDetailDto(ChattingDto chattingDto, Long memberId, Long meetingId) {
        super(chattingDto.getMemberId(), chattingDto.getMessage(), chattingDto.getChattingTime());
        this.meetingId = meetingId;
        if (chattingDto.getMemberId().equals(memberId)) this.isMine = true;
        else this.isMine = false;
    }

    public ChattingDetailDto(Chatting chatting, Long memberId) {
        super(chatting.getMember().getId(), chatting.getMessage(), chatting.getChattingTime());
        this.meetingId = chatting.getMeeting().getId();
        if (chatting.getMember().getId().equals(memberId)) this.isMine = true;
        else this.isMine = false;
    }
}
