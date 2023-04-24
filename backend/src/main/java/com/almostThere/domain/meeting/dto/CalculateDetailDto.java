package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.CalculateDetail;
import com.almostThere.domain.meeting.entity.CalculateType;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.user.dto.MemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalculateDetailDto {
    private Long id;
    private Meeting meeting;
    private CalculateType type;
    private String filePath;
    private String fileName;
    private String storeName;
    private int price;

    public CalculateDetailDto(CalculateDetail c) {
        this.id = c.getId();
        this.meeting = c.getMeeting();
        this.type = c.getType();
        this.filePath = c.getFilePath();
        this.fileName = c.getFileName();
        this.storeName = c.getStoreName();
        this.price = c.getPrice();
    }
}
