package com.almostThere.domain.user.controller;

import com.almostThere.domain.meeting.dto.MeetingCntDto;
import com.almostThere.domain.meeting.service.MeetingService;
import com.almostThere.domain.user.dto.MemberInfoDto;
import com.almostThere.domain.user.service.MemberService;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final MeetingService meetingService;

    /**
     * 자주 만나는 상위 9명의 멤버들을 조회한다.
     * @param request
     * @return
     */
    @GetMapping("/best-member")
    public BaseResponse getBestMember(HttpServletRequest request){
        List<MeetingCntDto> bestMember = memberService.findBestMember(1L);
        return BaseResponse.success(bestMember);
    }

    /**
     * 마이페이지에 필요한 회원정보를 조회한다.
     * @param
     * @return
     */
    @GetMapping("/info")
    public BaseResponse getMemberPageInfo() {
        logger.info("#[MemberController]# getMemberPageInfo - 마이페이지 회원정보 조회 동작");

        // ! 추후 token을 활용해 memeberId 추출
        Long memberId = Long.valueOf("1");

        // i) member Profile 정보
//        memberService.getMemberByMemberId(memberId);

        // ii) member의 모든 모임 이력 정보
        //     - 모든 모임 이력, [모임-멤버] 테이블에 멤버ID가 있는 모임ID 정보 가져오기
//        meetingService.findAttendAllMeetingById(memberId);

        // iii) 모임 data 요약 정보
        //      - 이번달 x번의 모임을 잡았는 지
        //      - 누적 x번 약속 중 y번 지각
        //      - 지난달 모임에서 150,000원 소비

//        #21# member가 참여한 meetingMembers: [AttendMeetingMemberDto
//        (meetingMemberId=1, spendMoney=12500.0, state=ARRIVE, meeting=Meeting(id=1, host=com.almostThere.domain.user.entity.Member@709ce129, meetingName=스터디, meetingTime=2023-04-28T16:53:08, meetingPlace=역삼역, meetingAddress=역삼역, meetingLat=37.5004, meetingLng=127.0361, lateAmount=1000, regdate=2023-04-27T16:53:08, roomCode=10000, meetingMembers=[com.almostThere.domain.meeting.entity.MeetingMember@717afd5c, com.almostThere.domain.meeting.entity.MeetingMember@35951b4], calculateDetails=[com.almostThere.domain.meeting.entity.CalculateDetail@529ec18b])),
//        (meetingMemberId=2, spendMoney=85000.0, state=LATE, meeting=Meeting(id=2, host=com.almostThere.domain.user.entity.Member@709ce129, meetingName=롯데월드, meetingTime=2023-03-29T16:53:08, meetingPlace=잠실역, meetingAddress=잠실역, meetingLat=37.5134, meetingLng=127.1001, lateAmount=5000, regdate=2023-04-27T16:53:08, roomCode=20000, meetingMembers=[com.almostThere.domain.meeting.entity.MeetingMember@376ec65a], calculateDetails=[com.almostThere.domain.meeting.entity.CalculateDetail@752681b1]))]

//        return BaseResponse.success(new MemberInfoDto(
//                memberService.getMemberByMemberId(memberId),
//                meetingService.findAttendAllMeetingById(memberId)));
//        return BaseResponse.success(meetingService.findAttendAllMeetingById(memberId));
        return BaseResponse.success(new MemberInfoDto(memberService.getMemberByMemberId(memberId)))
    }
}
