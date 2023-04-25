package com.almostThere.domain.meeting.dto.update;

import com.almostThere.domain.meeting.entity.Meeting;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MeetingUpdateRequestDto {

    private Long meetingId;
    private Long hostId;
    private String meetingName;
    private LocalDateTime meetingTime;
    private String meetingPlace;
    private String meetingAddress;
    private double meetingLat;
    private double meetingLng;
    private Integer lateAmount;
    private LocalDateTime regdate;
}
