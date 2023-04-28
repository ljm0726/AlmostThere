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
    
    // 이번달에 참석한 모임 개수
    private Integer attendMeetingCnt;

    // 누적(총) 모임 개수
    private Integer totalMeetingCnt;

    // 누적(총) 모임 중 지각 횟수
    private Integer totalLateCnt;

    // 지난달 모임에서 소비한 총 가격
    private Integer lastMonthTotalSpendMoney;

    public MemberInfoDto(Member member, List<MeetingDto> meetingDtoList, Integer attendMeetingCnt, Integer totalMeetingCnt, Integer totalLateCnt, Integer lastMonthTotalSpendMoney) {
        this.member = member;
        this.meetingDtoList = meetingDtoList;
        this.attendMeetingCnt = attendMeetingCnt;
        this.totalMeetingCnt = totalMeetingCnt;
        this.totalLateCnt = totalLateCnt;
        this.lastMonthTotalSpendMoney = lastMonthTotalSpendMoney;
    }
}
