package com.almostThere.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ChattingRequestDto implements Serializable {

    private String nickname;

    private String message;
}
