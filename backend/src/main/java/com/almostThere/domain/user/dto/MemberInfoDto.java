package com.almostThere.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberInfoDto {
    private MemberDto member;

    public MemberInfoDto(MemberDto member) {
        this.member = member;
    }
}