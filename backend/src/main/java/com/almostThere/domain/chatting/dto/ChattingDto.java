package com.almostThere.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ChattingDto {

    String meetingId;

    Long userId;

    String message;

    LocalDateTime regtime;
}
