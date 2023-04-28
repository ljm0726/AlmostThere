package com.almostThere.domain.user.dto;

import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.user.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class MemberInfoDto {

    // 회원 정보
    private Member member;

    // 회원이 참석한 모든 모임
    private List<MeetingDto> meetingDtoList;

    public MemberInfoDto(Member member, List<MeetingDto> meetingDtoList) {
        this.member = member;
        this.meetingDtoList = meetingDtoList;
    }
}
