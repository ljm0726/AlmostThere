package com.almostThere.domain.meeting.controller;

import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.dto.MeetingTimeDto;
import com.almostThere.domain.meeting.dto.create.MeetingCreateRequestDto;
import com.almostThere.domain.meeting.dto.delete.MeetingDeleteRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailResponseDto;
import com.almostThere.domain.meeting.dto.update.MeetingStartPlaceRequestDto;
import com.almostThere.domain.meeting.dto.update.MeetingUpdateRequestDto;
import com.almostThere.domain.meeting.service.MeetingService;
import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.NotFoundException;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingApiController {

    private final MeetingService meetingService;

    @GetMapping("/most-recent")
    public BaseResponse getMostRecentMeeting(Authentication authentication){

        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();
        MeetingTimeDto meetingTimeDto = meetingService.getMostRecentMeeting(memberId);

        return BaseResponse.success(meetingTimeDto);

    }
    /**
     * dyeon7310
     * @param
     * @return 오늘의 모임(24시간 이내)을 조회한다.
     */
    @GetMapping("/today")
    public BaseResponse getTodayMeetings(Authentication authentication){
        Long memberId = ((MemberAccessDto)authentication.getPrincipal()).getId();
        List<MeetingDto> todayMeeting = meetingService.findTodayMeeting(memberId);
        return BaseResponse.success(todayMeeting);
    }

    /**
     * dyeon7310
     * @param
     * @return 앞으로 한 달 이내의 모임을 조회한다.
     */
    @GetMapping("/upcoming")
    public BaseResponse getUpcomingMeetings(Authentication authentication){
        Long memberId = ((MemberAccessDto)authentication.getPrincipal()).getId();
        List<MeetingDto> upcomingMeeting = meetingService.findUpcomingMeeting(memberId);
        return BaseResponse.success(upcomingMeeting);
    }

    /**
     * asng
     * @param meetingCreateRequestDto
     * @return 모임을 생성한다.
     */
    @PostMapping
    public BaseResponse createMeeting(@RequestBody MeetingCreateRequestDto meetingCreateRequestDto){
        meetingService.createMeeting(meetingCreateRequestDto);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * asng
     * @param
     * @return 모임 상세정보를 조회한다.
     */
    @GetMapping("/detail/{meetingId}")
    public BaseResponse getMeetingDetail(Authentication authentication, @PathVariable Long meetingId) {
        MeetingDetailResponseDto meetingDetailResponseDto = null;
        try {
            Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();
            meetingDetailResponseDto = meetingService.getMeetingDetail(memberId, meetingId);
        }catch (NotFoundException e) {
            if(e.getErrorCode().equals(ErrorCode.MEETING_NOT_FOUND)){
                System.out.println("존재하지 않는 미팅입니다.");
                return BaseResponse.customFail(ErrorCode.MEETING_NOT_FOUND);
            }
            else if(e.getErrorCode().equals(ErrorCode.MEETING_MEMBER_NOT_FOUND)){
                System.out.println("존재하지 않는 멤버입니다");
                return BaseResponse.customFail(ErrorCode.MEETING_MEMBER_NOT_FOUND);
            }
        }
        return BaseResponse.success(meetingDetailResponseDto);
    }

    /**
     * asng
     * @param meetingDeleteRequestDto
     * @return 모임을 삭제한다.
     */
    @DeleteMapping
    public BaseResponse deleteMeeting(@RequestBody MeetingDeleteRequestDto meetingDeleteRequestDto){
        meetingService.deleteMeeting(meetingDeleteRequestDto);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * asng
     * @param meetingDeleteRequestDto
     * @return 모임방에서 나간다.
     */
    @PutMapping("/exit")
    public BaseResponse exitMeeting(@RequestBody MeetingDeleteRequestDto meetingDeleteRequestDto){
        meetingService.exitMeeting(meetingDeleteRequestDto);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * asng
     * @param meetingUpdateRequestDto
     * @return 모임 상세정보를 수정한다.
     */
    @PutMapping
    public BaseResponse updateMeeting(@RequestBody MeetingUpdateRequestDto meetingUpdateRequestDto){
        meetingService.updateMeeting(meetingUpdateRequestDto);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * halo
     * 모임ID + 멤버ID에 따른 출발장소를 수정한다.
     * @param meetingStartPlaceRequestDto
     * @return
     */
    @PostMapping("/start-place")
    public BaseResponse setStartPlace(@RequestBody MeetingStartPlaceRequestDto meetingStartPlaceRequestDto, Authentication authentication) {
        System.out.println("#[MemberController]# setStartPlace 출발장소 수정 - request: " + meetingStartPlaceRequestDto);

        // * token을 활용하여 현 로그인 member의 id 추출
        meetingStartPlaceRequestDto.setMemberId(((MemberAccessDto)authentication.getPrincipal()).getId());

        // 변경된 출발장소 update
        meetingService.updateMemberStartPlace(meetingStartPlaceRequestDto);

        return BaseResponse.success(null);
    }
}
