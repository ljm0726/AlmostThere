package com.almostThere.domain.meeting.service;

import com.almostThere.domain.meeting.dto.MeetingMemberDto;
import com.almostThere.domain.meeting.dto.delete.MeetingDeleteRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingCalculateDetailDto;
import com.almostThere.domain.meeting.dto.create.MeetingCreateRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailResponseDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.dto.detail.MeetingMemberResponseDto;
import com.almostThere.domain.meeting.dto.update.MeetingUpdateRequestDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.meeting.repository.MeetingMemberRepository;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.AccessDeniedException;
import com.almostThere.global.error.exception.NotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
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
    private final MemberRepository memberRepository;
    private final MeetingMemberRepository meetingMemberRepository;


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

        Meeting meeting = meetingCreateRequestDto.toEntity(meetingCreateRequestDto, meetingHost,roomCode);
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
        final Meeting meeting = meetingRepository.findById(meetingDetailRequestDto.getMeetingId())
            .orElseThrow(()->new NotFoundException(
                ErrorCode.MEETING_NOT_FOUND));
        List<MeetingMemberResponseDto> meetingMembers = meeting.getMeetingMembers()
            .stream().map(m->new MeetingMemberResponseDto(m)).collect(Collectors.toList());
        List<MeetingCalculateDetailDto> calculateDetails = new ArrayList<>();
        if(meeting.getCalculateDetails() != null) {
            calculateDetails = meeting.getCalculateDetails()
                .stream().map(c->new MeetingCalculateDetailDto(c)).collect(Collectors.toList());
        }

        return new MeetingDetailResponseDto(meeting,meetingMembers,calculateDetails);
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
    public List<MeetingMember> findAllMeeting(Long memberId) {
        // i) member가 참여한 모임ID List 찾기
        List<MeetingMember> meetingMembers = meetingMemberRepository.findByMemberId(memberId);

//        Optional<MeetingMember> meetingMembers = meetingMemberRepository.findById(memberId);
        return meetingMembers;
    }
}
