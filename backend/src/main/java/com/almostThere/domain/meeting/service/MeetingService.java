package com.almostThere.domain.meeting.service;

import com.almostThere.domain.meeting.dto.AttendMeetingMemberDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.dto.MeetingTimeDto;
import com.almostThere.domain.meeting.dto.create.MeetingCreateRequestDto;
import com.almostThere.domain.meeting.dto.delete.MeetingDeleteRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingCalculateDetailDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailResponseDto;
import com.almostThere.domain.meeting.dto.detail.MeetingMemberResponseDto;
import com.almostThere.domain.meeting.dto.update.MeetingStartPlaceRequestDto;
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
import org.springframework.data.domain.PageRequest;
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
    private final CalculateDetailService calculateDetailService;

    /**
     * 유저가 roomCode에 해당하는 모임에 가입해있는지 조회한다.
     * @param roomCode
     * @param memberId
     * @return 해당 모임의 meetingId를 반환한다.
     *          만약 가입되어 있지 않고 모임의 가입 가능 인원(10명)이 남았다면 가입시키고 meetingId를 반환하고,
     *          가입할 수 없다면 -1을 반환한다.
     */
    public Long checkAndSaveMeetingMember(String roomCode, Long memberId){

        Meeting meeting = meetingRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_NOT_FOUND));
        List<MeetingMember> meetingMembers = meeting.getMeetingMembers();

        boolean isJoined = false;
        for(MeetingMember meetingMember : meetingMembers){
            if(meetingMember.getMember().getId() == memberId){
                isJoined = true;
                break;
            };
        }

        Long meetingId = meeting.getId();
        if(!isJoined && meetingMembers.size() < 10){
            Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
            MeetingMember meetingMember = new MeetingMember(member, meeting, StateType.GOING);
            meetingMemberRepository.save(meetingMember);
            calculateDetailService.updateSpentMoney(meeting);
        }else if(meetingMembers.size() > 10){
            meetingId = -1L;
        }
        return meetingId;
    }
    /**
     * 지금 시간 이후로 가장 빠른 미팅 조회
     * @param memberId
     * @return
     */
    public MeetingTimeDto getMostRecentMeeting(Long memberId){

        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Meeting> meeting = meetingRepository.getMostRecentMeeting(memberId, pageRequest);
        if(meeting.size()==1) {
            MeetingTimeDto meetingTimeDto = new MeetingTimeDto(meeting.get(0));
            System.out.println("가장 최근 모임의 시간 :"+meetingTimeDto.getMeetingTime());
            return meetingTimeDto;
        }

        return null;
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
//        meetingCreateRequestDto.setMeetingTime(meetingCreateRequestDto.getMeetingTime().plusHours(9));
        meetingCreateRequestDto.setMeetingTime(meetingCreateRequestDto.getMeetingTime());
        Meeting meeting = meetingCreateRequestDto.toEntity(meetingCreateRequestDto, meetingHost,rc);
        meeting = meetingRepository.save(meeting);
        MeetingMember meetingMember = new MeetingMember(meetingHost, meeting, StateType.GOING);
        meetingMemberRepository.save(meetingMember);
    }

    /**
     * 모임상세정보, 모임멤버, 정산상세내역을 조회한다.
     * @param
     * @return 모임 상세정보
     */
    public MeetingDetailResponseDto getMeetingDetail(Long memberId, Long meetingId){
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));
        MeetingMember meetingMember = meetingMemberRepository.findByMeeting_IdAndMember_Id(
                meetingId,
                memberId
        ).orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_MEMBER_NOT_FOUND));

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
        final Meeting meeting = meetingRepository.findById(meetingDeleteRequestDto.getMeetingId())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getHost().getId() == meetingDeleteRequestDto.getMeetingId())
            meetingRepository.deleteById(meetingDeleteRequestDto.getMeetingId());
        else throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZATION);
    }

    /**
     * 모임에서 나간다.
     * @param meetingDeleteRequestDto
     */
    @Transactional
    public void exitMeeting(MeetingDeleteRequestDto meetingDeleteRequestDto){
        meetingMemberRepository.deleteMeetingMemberByMeetingIdAndMemberID(
                meetingDeleteRequestDto.getMemberId(),meetingDeleteRequestDto.getMeetingId());
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

    /**
     * 모임ID + 멤버ID에 따른 출발장소를 수정한다.
     * @param
     */
    @Transactional
    public void updateMemberStartPlace(MeetingStartPlaceRequestDto meetingStartPlaceRequestDto){
        // i) 로그인 한 사용자의 meeting-member 조회
        MeetingMember meetingMember = meetingMemberRepository.findByMeeting_IdAndMember_Id(
                meetingStartPlaceRequestDto.getMeetingId(),
                meetingStartPlaceRequestDto.getMemberId()
        ).orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        // ii) 변경된 출발장소 set
        meetingMember.updateStartPlace(meetingStartPlaceRequestDto);
        // iii) meeting-member 저장
        meetingMemberRepository.save(meetingMember);
    }

    /**
     * 현재 시각을 기준으로 최근에 지난 모임시간을 조회한다.
     * @param
     */
    public MeetingTimeDto getRecentPastMeeting(Long memberId){
        List<Meeting> meeting = meetingRepository.getRecentPastMeeting(memberId, PageRequest.of(0, 1));
        if (meeting.size() != 0) {
            return new MeetingTimeDto(meeting.get(0));
        }

        return null;
    }
}
