package com.almostThere.domain.meeting.repository;

import com.almostThere.domain.meeting.entity.MeetingCnt;
import com.almostThere.domain.user.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingCntRepository extends JpaRepository<MeetingCnt, Long> {
    Page<MeetingCnt> findBymyMemberOrderByCntDesc(Member member, Pageable pageable);
}
