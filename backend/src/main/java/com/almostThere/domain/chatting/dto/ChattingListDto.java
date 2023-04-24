package com.almostThere.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ChattingListDto {

    private List<ChattingDetailDto> chattingDetailDtoList;

    private Long lastNumber;
}
