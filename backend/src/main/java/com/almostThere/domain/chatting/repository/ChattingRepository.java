package com.almostThere.domain.chatting.repository;

import com.almostThere.domain.chatting.entity.Chatting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {

    Long countByMeeting_Id(Long Id);

    Page<Chatting> findAllByMeeting_IdOrderByChattingTimeDescIdDesc(Long id, Pageable pageable);
}