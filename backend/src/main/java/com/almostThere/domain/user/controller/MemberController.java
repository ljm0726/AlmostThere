package com.almostThere.domain.user.controller;

import com.almostThere.domain.meeting.dto.MeetingCntDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.service.MeetingService;
import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.domain.user.dto.MemberInfoDto;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.service.MemberService;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public BaseResponse<?> getMemberPageInfo() {
        logger.info("#[MemberController]# getMemberPageInfo - 마이페이지 회원정보 조회 동작");

        // ! 추후 token을 활용해 memeberId 추출
        Long memberId = Long.valueOf("1");

        // i) member Profile 정보
//        Member member = memberService.getMemberByMemberId(memberId);
        // ii) member의 모든 모임 이력 정보
        //     - 모든 모임 이력, [모임-멤버] 테이블에 멤버ID가 있는 모임ID 정보 가져오기
        List<MeetingDto> meetingDtoList = meetingService.findAllMeeting(memberId);
        // iii) 모임 data 요약 정보
        //      - 이번달 x번의 모임을 잡았는 지
        Integer attendMeetingCnt = 0;
        //      - 누적 x번 약속 중 y번 지각
        Integer totalLateCnt = 0;
        //      - 지난달 모임에서 150,000원 소비
        Integer lastMonthTotalSpendMoney = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (MeetingDto meetingDto: meetingDtoList) {
            LocalDateTime meetingTime = LocalDateTime.parse(meetingDto.getMeetingTime(), formatter);
            // 이번달 - 모임 개수
            if (meetingTime.getMonthValue() == LocalDate.now().getMonthValue()) {
                attendMeetingCnt++;
            }
            // 지난달 - 모임 소비 가격
            else if (meetingTime.getMonthValue() == LocalDate.now().minusMonths(1).getMonthValue()) {
                lastMonthTotalSpendMoney += meetingDto.getCalculateDetails().get(0).getPrice();
            }

            // 총 모임 중 지각 횟수
            if (meetingDto.getMeetingMembers().get(0).getState().equals("LATE"))
                totalLateCnt++;
        }

//        return BaseResponse.success(new MemberInfoDto(memberService.getMemberByMemberId(memberId), meetingService.findAllMeeting(memberId)));
        return BaseResponse.success(new MemberInfoDto(
                memberService.getMemberByMemberId(memberId),
                meetingDtoList,
                attendMeetingCnt,
                meetingDtoList.size(),
                totalLateCnt,
                lastMonthTotalSpendMoney));
    }
}
