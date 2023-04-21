package com.almostThere.domain.chatting.service;

import com.almostThere.domain.chatting.dto.ChattingDto;
import com.almostThere.domain.chatting.dto.ChattingMemberDto;
import com.almostThere.domain.chatting.repository.ChattingJDBCRepository;
import com.almostThere.domain.chatting.repository.ChattingRepository;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChattingService {

    @Autowired
    RedisTemplate redisTemplate;

    private final ChattingRepository chattingRepository;

    private final ChattingJDBCRepository chattingJDBCRepository;

    private final MeetingRepository meetingRepository;

    /**
     * @param memberId 멤버ID
     * @param meetingId 미팅ID
     * @param message 채팅 내용
     * @param now 채팅 전송 시간
     * @return 저장한 채팅 정보를 반환한다.
     * **/
    public ChattingDto addChattingRedis(Long memberId, Long meetingId, String message, LocalDateTime now) {

        // ChattingDto 생성
        ChattingDto chattingDto = new ChattingDto(meetingId, memberId, message, now);

        // redis에 ChattingDto 저장
        ListOperations<Long, ChattingDto> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(meetingId, chattingDto);

        return chattingDto;
    }

    /**
     * @return 1시간마다 Redis의 채팅을 MySQL에 저장한다.
     * **/
//    @Scheduled(cron = "0 0 0/1 * * *") // 1시간 주기
    @Scheduled(cron = "0 * * * * *") // 테스트 위해 1분 주기
    @Transactional
    public void addChattingMysql() {
        System.out.println("# Scheduled 실행 #");

        // key 가져오기
        // 성능 향상 위해 keys() 대신 scan() 사용
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        Cursor<Long> cursor = redisTemplate.scan(scanOptions);
        ListOperations<Long, ChattingDto> listOperations = redisTemplate.opsForList();
        
        // 가져온 key를 반복하며 각 key에 있는 값 MySQL에 저장하기
        while (cursor.hasNext()) {
            
            // key 값
            Long key = cursor.next();

            // redis에서 key 해당하는 모든 값 가져오기
            Long size = listOperations.size(key);
            List<ChattingDto> chattingDtoList = listOperations.range(key, 0, listOperations.size(key));
            
            // mysql에 저장 - batchInsert 여러 행을 한 번에 넣기
            // 성능 관련 참고자료 https://datamoney.tistory.com/319
            chattingJDBCRepository.batchInsert(chattingDtoList);

            // 가져온 값 redis에서 삭제
            listOperations.leftPop(key, size);
        }
    }

    /**
     * @param meetingId 미팅ID
     * @return 미팅 및 채팅 관련 정보를 가져온다.
     * **/
    public ChattingDto getChattingInfo(Long meetingId) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meetingId);
        if (optionalMeeting.isPresent()) {
            Meeting meeting = optionalMeeting.get();
            
        }
        return null;
    }

    /**
     * @param meetingId 미팅ID
     * @return 기록 30개를 조회한다.
     * **/
    public Map<Long, ChattingMemberDto> getChatting30(Long meetingId) {

        // redis에서 meetingId의 채팅 정보를 가져온다.
        ListOperations<Long, ChattingDto> listOperations = redisTemplate.opsForList();
        List<ChattingDto> result = listOperations.range(meetingId, 0, listOperations.size(meetingId));

        return null;
    }
}