package com.almostThere.domain.chatting.repository;

import com.almostThere.domain.chatting.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {

    List<Chatting> findByMeeting_MeetingId(Long meetingId);
}
