package com.almostThere.domain.meeting.dto.detail;

import com.almostThere.domain.meeting.entity.Meeting;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingDetailResponseDto {

    private Long hostId;
    private Long meetingId;
    private String meetingName;
    private String meetingPlace;
    private LocalDateTime meetingTime;
    private String meetingAddress;
    private double meetingLat;
    private double meetingLng;
    private Integer lateAmount;
    private LocalDateTime regDate;
    private int roomCode;
    private List<MeetingMemberResponseDto> meetingMembers;
    private List<MeetingCalculateDetailDto> meetingCalculateDetails;

    public MeetingDetailResponseDto(Meeting meeting
        , List<MeetingMemberResponseDto> meetingMembers, List<MeetingCalculateDetailDto> meetingCalculateDetails) {
        this.hostId = meeting.getHost().getId();
        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.meetingPlace = meeting.getMeetingPlace();
        this.meetingTime = meeting.getMeetingTime();
        this.meetingAddress = meeting.getMeetingAddress();
        this.meetingLat = meeting.getMeetingLat();
        this.meetingLng = meeting.getMeetingLng();
        this.lateAmount = meeting.getLateAmount();
        this.regDate = meeting.getRegdate();
        this.roomCode = meeting.getRoomCode();
        this.meetingMembers = meetingMembers;
        this.meetingCalculateDetails = meetingCalculateDetails;
    }
}
