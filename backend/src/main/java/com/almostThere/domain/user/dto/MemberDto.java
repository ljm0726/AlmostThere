package com.almostThere.domain.user.dto;

import com.almostThere.domain.user.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String memberEmail;
    private String memberProfileImg;
    private String memberNickname;
    private LocalDateTime regdate;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.memberEmail = member.getMemberEmail();
        this.memberProfileImg = member.getMemberProfileImg();
        this.memberNickname = member.getMemberNickname();
        this.regdate = member.getRegdate();
    }
}
