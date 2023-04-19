package com.almostThere.domain.meeting.controller;

import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.service.MeetingService;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingApiController {

    private final MeetingService meetingService;
    /**
     * dyeon7310
     * @param request
     * @return 오늘의 모임(24시간 이내)을 조회한다.
     */
    @GetMapping("/today")
    public BaseResponse getTodayMeetings(HttpServletRequest request){
        List<MeetingDto> todayMeeting = meetingService.findTodayMeeting(1L);
        return BaseResponse.success(todayMeeting);
    }

    /**
     * dyeon7310
     * @param request
     * @return 앞으로 한 달 이내의 모임을 조회한다.
     */
    @GetMapping("/upcoming")
    public BaseResponse getUpcomingMeetings(HttpServletRequest request){
        List<MeetingDto> upcomingMeeting = meetingService.findUpcomingMeeting(1L);
        return BaseResponse.success(upcomingMeeting);
    }
}
