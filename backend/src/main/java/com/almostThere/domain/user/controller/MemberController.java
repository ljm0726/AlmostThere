package com.almostThere.domain.user.controller;

import com.almostThere.domain.meeting.dto.MeetingCntDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.service.MeetingService;
import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.domain.user.dto.MemberInfoDto;
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
        // ! 추후 token을 활용해 memeberId 추출
        Long memberId = Long.valueOf("1");

        // i) member Profile 정보
        MemberInfoDto memberInfoDto = memberService.getMemberByMemberId(memberId);
        logger.info("#21# memberInfo 확인: {}", memberInfoDto);
        // ii) member의 모든 모임 이력 정보
        //     - 모든 모임 이력, [모임-멤버] 테이블에 멤버ID가 있는 모임ID 정보 가져오기
//        List<MeetingDto> meetingIdList = memberService.findMeetingList(memberId);
        meetingService.findMeetingList(memberId);


//        return BaseResponse.success(new MemberInfoDto(
//        ));
        return BaseResponse.success(null);
    }
}
