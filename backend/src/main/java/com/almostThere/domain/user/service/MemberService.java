package com.almostThere.domain.user.service;

import com.almostThere.domain.meeting.dto.MeetingCntDto;
import com.almostThere.domain.meeting.entity.MeetingCnt;
import com.almostThere.domain.meeting.repository.MeetingCntRepository;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MeetingCntRepository meetingCntRepository;
    private final MemberRepository memberRepository;
    public List<MeetingCntDto> findBestMember(Long memberId){
        Member member = memberRepository.findByid(memberId);
        System.out.println(member.getId()+" "+member.getMemberNickname());
        Page<MeetingCnt> bestMember = meetingCntRepository.findBymyMemberOrderByCntDesc(member, PageRequest.of(0, 9));
        List<MeetingCntDto> result = bestMember.stream().map(m -> new MeetingCntDto(m)).collect(Collectors.toList());
        return result;
    }
}
