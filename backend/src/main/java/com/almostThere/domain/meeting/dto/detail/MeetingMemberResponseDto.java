package com.almostThere.domain.meeting.dto.detail;

import com.almostThere.domain.meeting.entity.MeetingMember;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingMemberResponseDto {

    private Long memberId;
    private String memberEmail;
    private String memberNickname;
    private String memberProfileImg;

    public MeetingMemberResponseDto(MeetingMember meetingMember) {
        this.memberId = meetingMember.getMember().getId();
        this.memberEmail = meetingMember.getMember().getMemberEmail();
        this.memberNickname = meetingMember.getMember().getMemberNickname();
        this.memberProfileImg = meetingMember.getMember().getMemberProfileImg();
    }
}
