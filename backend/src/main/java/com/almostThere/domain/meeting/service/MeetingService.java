package com.almostThere.domain.meeting.service;

import com.almostThere.domain.meeting.dto.AttendMeetingMemberDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.dto.create.MeetingCreateRequestDto;
import com.almostThere.domain.meeting.dto.delete.MeetingDeleteRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingCalculateDetailDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailResponseDto;
import com.almostThere.domain.meeting.dto.detail.MeetingMemberResponseDto;
import com.almostThere.domain.meeting.dto.update.MeetingUpdateRequestDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.meeting.repository.CalculateDetailRepository;
import com.almostThere.domain.meeting.repository.MeetingMemberRepository;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.AccessDeniedException;
import com.almostThere.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingService {
    private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final CalculateDetailRepository calculateDetailRepository;

    /**
     * 멤버가 참여중인 모임 중 약속시간이 3시간 이내에 있는 모임의 수를 조회한다.
     * @param memberId
     * @return 모임의 수
     */
    public int countMeetingWithin3hours(Long memberId){
        LocalDateTime afterDate = LocalDateTime.now().plusHours(3);
        System.out.println(afterDate);
        return meetingRepository.countMeetingsWithin3hours(memberId,afterDate);
    }

    /**
     * 멤버가 참여한 오늘 예정인 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<MeetingDto> findTodayMeeting(Long memberId){
        LocalDateTime afterDate = LocalDateTime.now().plusDays(1);
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

    /**
     * 모임을 만드는 사람을 host로 모임을 생성한다.
     * @param meetingCreateRequestDto
     * @return 없음
     */
    @Transactional
    public void createMeeting(MeetingCreateRequestDto meetingCreateRequestDto){
        final Member meetingHost = memberRepository.findById(meetingCreateRequestDto.getHostId())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEMBER_NOT_FOUND));

        Random rand = new Random();
        int roomCode = (int)(rand.nextLong()%100000000L);
        roomCode = Math.abs(roomCode);
        String rc = Integer.toString(roomCode);
        Meeting meeting = meetingCreateRequestDto.toEntity(meetingCreateRequestDto, meetingHost,rc);
        meeting = meetingRepository.save(meeting);
        MeetingMember meetingMember = new MeetingMember(meetingHost, meeting, StateType.GOING);
        meetingMemberRepository.save(meetingMember);
    }

    /**
     * 모임상세정보, 모임멤버, 정산상세내역을 조회한다.
     * @param meetingDetailRequestDto
     * @return 모임 상세정보
     */
    public MeetingDetailResponseDto getMeetingDetail(MeetingDetailRequestDto meetingDetailRequestDto){
        Long memberId = meetingDetailRequestDto.getMemberId();
        final Meeting meeting = meetingRepository.findById(meetingDetailRequestDto.getMeetingId())
                .orElseThrow(()->new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));

        List<MeetingMemberResponseDto> meetingMembers = meeting.getMeetingMembers()
                .stream().map(m->new MeetingMemberResponseDto(m)).collect(Collectors.toList());
        MeetingMember member = meeting.getMeetingMembers().stream().filter(m->m.getMember().getId()==memberId).findAny().get();

        List<MeetingCalculateDetailDto> calculateDetails = new ArrayList<>();
        if(meeting.getCalculateDetails() != null) {
            calculateDetails = meeting.getCalculateDetails()
                    .stream().map(c->new MeetingCalculateDetailDto(c)).collect(Collectors.toList());
        }
        int totalPrice = calculateDetailRepository.sumMeetingPrice(meeting.getId());
        int memberPrice = meetingMemberRepository.sumMemberPrice(meeting.getId());
        int remain = totalPrice-memberPrice;
        return new MeetingDetailResponseDto(meeting, remain, meetingMembers, calculateDetails);
    }

    /**
     * Host가 모임을 삭제한다.
     * @param meetingDeleteRequestDto
     */
    @Transactional
    public void deleteMeeting(MeetingDeleteRequestDto meetingDeleteRequestDto) {
        final Meeting meeting = meetingRepository.findById(meetingDeleteRequestDto.getMeetingid())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getHost().getId() == meetingDeleteRequestDto.getMemberid())
            meetingRepository.deleteById(meetingDeleteRequestDto.getMeetingid());
        else throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZATION);
    }

    /**
     * 모임에서 나간다.
     * @param meetingDeleteRequestDto
     */
    @Transactional
    public void exitMeeting(MeetingDeleteRequestDto meetingDeleteRequestDto){
        meetingMemberRepository.deleteMeetingMemberByMeetingIdAndMemberID(
                meetingDeleteRequestDto.getMemberid(),meetingDeleteRequestDto.getMeetingid());
    }

    /**
     * 모임 상세정보를 수정한다.
     * @param meetingUpdateRequestDto
     */
    @Transactional
    public void updateMeeting(MeetingUpdateRequestDto meetingUpdateRequestDto){
        final Meeting meeting = meetingRepository.findById(meetingUpdateRequestDto.getMeetingId())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getHost().getId() == meetingUpdateRequestDto.getHostId()) {
            meeting.updateMeeting(meetingUpdateRequestDto);
            meetingRepository.save(meeting);
        }   else throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZATION);
    }

    /**
     * 멤버가 참여한 모든 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<AttendMeetingMemberDto> findAttendAllMeetingById(Long memberId) {
        // member가 참여한 모임멤버(+ 모임) List 조회
        return meetingMemberRepository.findByMemberId(memberId).stream().map(m -> new AttendMeetingMemberDto(m)).collect(Collectors.toList());
    }
}
