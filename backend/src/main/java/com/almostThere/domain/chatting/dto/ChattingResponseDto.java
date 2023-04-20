package com.almostThere.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChattingResponseDto {

    private String meetingId;

    private List<ChattingRequestDto> chatList;
}
