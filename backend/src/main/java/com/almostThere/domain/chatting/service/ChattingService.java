package com.almostThere.domain.chatting.service;

import com.almostThere.domain.chatting.dto.ChattingDetailDto;
import com.almostThere.domain.chatting.dto.ChattingDto;
import com.almostThere.domain.chatting.dto.ChattingListDto;
import com.almostThere.domain.chatting.dto.ChattingResponseDto;
import com.almostThere.domain.chatting.entity.Chatting;
import com.almostThere.domain.chatting.repository.ChattingJDBCRepository;
import com.almostThere.domain.chatting.repository.ChattingRepository;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final RedisTemplate<Long, ChattingDto> redisTemplate;

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
//    @Scheduled(cron = "0 * * * * *") // 테스트 위해 1분 주기
    @Scheduled(cron = "0 0/10 * * * *") // 10분 주기
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
            
            // 값이 1개 이상 있는 경우
            if (size > 0) {
                List<ChattingDto> chattingDtoList = listOperations.range(key, 0, listOperations.size(key));

                System.out.println("# chattingList size # "+chattingDtoList.size());

                // mysql에 저장 - batchInsert 여러 행을 한 번에 넣기
                // 성능 관련 참고자료 https://datamoney.tistory.com/319
                chattingJDBCRepository.batchInsert(chattingDtoList);

                // 가져온 값 redis에서 삭제
//                listOperations.leftPop(key, size);
                for (int i=0; i<size; i++) listOperations.leftPop(key);
            }
        }
    }

    /**
     * @param meetingId 미팅ID
     * @return 미팅 정보를 가져온다.
     * **/
    public ChattingResponseDto getChattingInfo(Long meetingId) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meetingId);
        if (optionalMeeting.isPresent()) {
            
            // 채팅 리스트를 제외한 모든 정보 가져오기
            Meeting meeting = optionalMeeting.get();
            ChattingResponseDto chattingResponseDto = new ChattingResponseDto(meeting);
            return chattingResponseDto;
        }
        return null;
    }

    /**
     * @param meetingId 미팅ID
     * @return 기록 30개를 조회한다.
     * **/
    public ChattingListDto getChattingLog(Long meetingId, Long memberId, Long lastNumber) {

        // redis에서 meetingId의 채팅 정보를 가져온다.
        ListOperations<Long, ChattingDto> listOperations = redisTemplate.opsForList();

        // 꺼내야 하는 개수
        int default_num = 20;
        // meetingId에 해당하는 채팅의 크기 확인
        Long redisSize = listOperations.size(meetingId);
        // MySQL에 저장된 값 가져오기
        Long mysqlSize = chattingRepository.countByMeeting_Id(meetingId);
        // 전체 개수
        Long length = redisSize + mysqlSize;
        if (lastNumber < 0) lastNumber = length;
        
        // 100개가 있어 처음에는 lastNumber 100번이야
        // 그럼 71번 ~ 100번을 가져와야지
        // mysqlSize 80, redisSize 20
        // mysql에 80개, redis에 20개가 있다고 생각하면서 작성

        // chattingDtoList 생성
        List<ChattingDetailDto> chattingDetailDtoList = new ArrayList<>();
        
        // Redis에 저장된 값을 가져와야 하는 경우
        if (lastNumber > mysqlSize) {

            // Redis Index
            Long redisIdx = lastNumber - mysqlSize;

            // Redis에 저장된 값으로 충분한 경우
            if (redisIdx >= default_num) {
                chattingDetailDtoList.addAll(listOperations.range(meetingId, redisIdx - default_num + 1, redisIdx).stream().map(m -> new ChattingDetailDto(m, memberId)).collect(Collectors.toList()));
                return new ChattingListDto(chattingDetailDtoList, redisIdx - default_num);
            }
            
            // Redis에 저장된 값으로 충분하지 않나, Redis에 저장된 값도 가져와야 하는 경우
            else {
                chattingDetailDtoList.addAll(listOperations.range(meetingId, 0, redisIdx).stream().map(m -> new ChattingDetailDto(m, memberId)).collect(Collectors.toList()));
                lastNumber -= redisIdx;
            }
        }

        // MySQL에 저장된 값 가져오기
        int page = (int) ((mysqlSize - lastNumber) / default_num);
        PageRequest pageRequest = PageRequest.of(page, default_num);
        Page<Chatting> chattingPage = chattingRepository.findAllByMeeting_IdOrderByChattingTimeAsc(meetingId, pageRequest);
        chattingDetailDtoList.addAll(0, chattingPage.getContent().stream().map(m -> new ChattingDetailDto(m, memberId)).collect(Collectors.toList()));

        lastNumber -= default_num;

//        else if (size < default_num*page && size > default_num*(page-1)) {
//            chattingDetailDtoList.addAll(listOperations.range(meetingId, default_num*(page-1), size).stream().map(m -> new ChattingDetailDto(m, memberId)).collect(Collectors.toList()));
//            num -= (size-default_num*(page-1));
//        }
//
//        // MySQL에 저장된 값 가져오기
//        Long length = chattingRepository.countByMeeting_Id(meetingId);
//
//        if (size + length >= default_num*page) {
//            //
//        }
//        else if (size + length < default_num*page && size + length > default_num*(page-1)) {
//            //
//        }

        return new ChattingListDto(chattingDetailDtoList, lastNumber);
    }
}