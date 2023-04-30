package com.almostThere.domain.user.dto;

import com.almostThere.domain.meeting.dto.AttendMeetingMemberDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberInfoDto {

//    private MemberDto member;
//
//    private List<MeetingDto> meetings;
//
//    public MemberInfoDto(MemberDto member, List<MeetingDto> meetings) {
//        this.member = member;
//        this.meetings = meetings;
//    }
    private MemberDto member;

    private List<AttendMeetingMemberDto> attendMeetingMemebers;

    public MemberInfoDto(MemberDto member, List<AttendMeetingMemberDto> attendMeetingMemebers) {
        this.member = member;
        this.attendMeetingMemebers = attendMeetingMemebers;
    }
}