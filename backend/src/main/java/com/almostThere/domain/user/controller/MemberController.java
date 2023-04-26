package com.almostThere.domain.user.controller;

import com.almostThere.domain.meeting.dto.MeetingCntDto;
import com.almostThere.domain.user.service.MemberService;
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
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

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
}
