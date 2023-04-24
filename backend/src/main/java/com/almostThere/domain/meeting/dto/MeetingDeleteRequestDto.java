package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.MeetingCnt;
import com.almostThere.domain.user.dto.MemberDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class MeetingDeleteRequestDto {

    private Long memberid;

    private Long meetingid;
}
