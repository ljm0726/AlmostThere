package com.almostThere.domain.meeting.repository;

import com.almostThere.domain.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//내장 CRUD 이용시 이 인터페이스를 통해 호출
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
