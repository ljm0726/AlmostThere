package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.user.dto.MemberDto;
import com.almostThere.domain.user.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class MeetingDto {
    private Long id;
    private MemberDto host;
    private String meetingName;
    private String meetingTime;
    private String meetingPlace;
    private double meetingLat;
    private double meetingLng;
    private int lateAmount;
    private String regdate;
    private int roomCode;

    public MeetingDto(Meeting meeting) {
        this.id = meeting.getId();
        this.host = new MemberDto(meeting.getHost());
        this.meetingName = meeting.getMeetingName();
        this.meetingTime = meeting.getMeetingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
        this.meetingPlace = meeting.getMeetingPlace();
        this.meetingLat = meeting.getMeetingLat();
        this.meetingLng = meeting.getMeetingLng();
        this.lateAmount = meeting.getLateAmount();
        this.regdate = meeting.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
        this.roomCode = meeting.getRoomCode();
    }
}
