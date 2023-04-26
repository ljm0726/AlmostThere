package com.almostThere.domain.meeting.service;

import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    /**
     * 멤버가 참여한 오늘 예정인 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<MeetingDto> findTodayMeeting(Long memberId){
        LocalDateTime afterDate = LocalDateTime.now().plusDays(1);
        System.out.println(afterDate);
        List<Meeting> meetings = meetingRepository.findTodayMeetings(memberId,afterDate);
        List<MeetingDto> result = meetings.stream().map(m->new MeetingDto(m)).collect(Collectors.toList());
        return result;
    }

    /**
     * 멤버가 참여한 한 달 이내의 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<MeetingDto> findUpcomingMeeting(Long memberId){
        LocalDateTime oneMonthAfterDate = LocalDateTime.now().plusMonths(1);
        LocalDateTime oneDayAfterDate = LocalDateTime.now().plusDays(1);
        System.out.println(oneDayAfterDate);
        System.out.println(oneMonthAfterDate);
        List<Meeting> meetings = meetingRepository.findUpcomingMeetings(memberId,oneDayAfterDate,oneMonthAfterDate);
        List<MeetingDto> result = meetings.stream().map(m->new MeetingDto(m)).collect(Collectors.toList());
        return result;
    }
}
