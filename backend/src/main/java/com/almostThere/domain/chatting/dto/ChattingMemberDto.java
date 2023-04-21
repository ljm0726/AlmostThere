package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.user.entity.Member;

public class ChattingMemberDto {

    // 프로필 사진
    private String profile;
    
    // 닉네임
    private String nickname;

    public ChattingMemberDto(MeetingMember meetingMember) {
        Member member = meetingMember.getMember();
        this.profile = member.getMemberProfileImg();
        this.nickname = member.getMemberNickname();
    }
}