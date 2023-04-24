package com.almostThere.domain.meeting.repository;

import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    @Modifying
    @Query("delete" +
        " from MeetingMember m " +
        "where m.member.id=:memberId " +
        "and m.meeting.id=:meetingId")
    void deleteMeetingMemberByMeetingIdAndMemberID(@Param("memberId") Long memberId, @Param("meetingId") Long meetingId);
}
