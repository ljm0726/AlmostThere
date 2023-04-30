package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.user.dto.MemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendMeetingMemberDto {
    private Long meetingMemberId;
    private Double spendMoney;
    private StateType state;
    private Meeting meeting;

    public AttendMeetingMemberDto(MeetingMember m) {
        this.meetingMemberId = m.getId();
        this.spendMoney = m.getSpentMoney();
        this.state = m.getState();
        this.meeting = m.getMeeting();
    }
}
