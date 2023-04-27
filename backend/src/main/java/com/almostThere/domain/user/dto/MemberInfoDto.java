package com.almostThere.domain.user.dto;

import com.almostThere.domain.user.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class MemberInfoDto {
    private Long id;
    private String memberProfileImg;
    private String memberNickname;

    public MemberInfoDto(Member member) {
        this.id = member.getId();
        this.memberProfileImg = member.getMemberProfileImg();
        this.memberNickname = member.getMemberNickname();
    }
}
