package com.almostThere.domain.chatting.service;

import com.almostThere.domain.chatting.dto.*;
import com.almostThere.domain.chatting.entity.Chatting;
import com.almostThere.domain.chatting.repository.ChattingJDBCRepository;
import com.almostThere.domain.chatting.repository.ChattingRepository;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.repository.MeetingMemberRepository;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.AccessDeniedException;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final RedisTemplate<Long, ChattingDto> redisTemplateDto;

    private final ChattingRepository chattingRepository;

    private final ChattingJDBCRepository chattingJDBCRepository;

    private final MeetingRepository meetingRepository;

    private final MemberRepository memberRepository;

    private final MeetingMemberRepository meetingMemberRepository;

    /**
     * @param meetingId 미팅ID
     * @param memberId 멤버ID
     * **/
    public void isChattingMember(Long meetingId, Long memberId) {

        // 해당 채팅방의 멤버가 맞는지 확인
        Optional<MeetingMember> optionalMeetingMember = meetingMemberRepository.findByMeeting_IdAndMember_Id(memberId, meetingId);
        if (!optionalMeetingMember.isPresent()) throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZATION);
    }

    /**
     * @param memberId 멤버ID
     * @param meetingId 미팅ID
     * @param message 채팅 내용
     * @param now 채팅 전송 시간
     * @return 저장한 채팅 정보를 반환한다.
     * **/
    public ChattingDto addChattingRedis(Long memberId, Long meetingId, String message, LocalDateTime now) {

        // ChattingDto 생성
        ChattingDto chattingDto = new ChattingDto(memberId, message, now);

        // redis에 ChattingDto 저장
        ListOperations<Long, ChattingDto> listOperations = redisTemplateDto.opsForList();
        listOperations.rightPush(meetingId, chattingDto);

        return chattingDto;
    }

    /**
     * @return 일정 시간마다 Redis에 저장해 둔 채팅을 MySQL에 저장한다.
     * **/
//    @Scheduled(cron = "0 0 0/1 * * *") // 1시간 주기
//    @Scheduled(cron = "0 0/10 * * * *") // 10분 주기
    @Scheduled(cron = "0 * * * * *") // 테스트 위해 1분 주기
    @Transactional
    public void addChattingMysql() {
        System.out.println("# Scheduled 실행 #");

        // key 가져오기
        // 성능 향상 위해 keys() 대신 scan() 사용
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        Cursor<Long> cursor = redisTemplateDto.scan(scanOptions);
        ListOperations<Long, ChattingDto> listOperations = redisTemplateDto.opsForList();
        
        // 가져온 key를 반복하며 각 key에 있는 값 MySQL에 저장하기
        while (cursor.hasNext()) {
            
            // key 값
            Long key = cursor.next();

            // redis에서 key 해당하는 모든 값 가져오기
            Long size = listOperations.size(key);
            
            // 값이 1개 이상 있는 경우
            if (size > 0) {
                List<ChattingDto> chattingDtoList = listOperations.range(key, 0, listOperations.size(key));

                // mysql에 저장 - batchInsert 여러 행을 한 번에 넣기
                // 48초에 10만 건
                // 성능 관련 참고자료 https://datamoney.tistory.com/319
                chattingJDBCRepository.batchInsert(chattingDtoList, key);

                // 가져온 값 redis에서 삭제
                // listOperations.leftPop(key, size); Redis 버전 호환 불가
                for (int i=0; i<size; i++) listOperations.leftPop(key);
            }
        }
    }

    /**
     * @param meetingId 미팅ID
     * @return 미팅 정보를 가져온다.
     * **/
    public ChattingResponseDto getChattingInfo(Long meetingId) {
        
        // ID에 해당하는 meeting 정보 가져오기
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meetingId);
        if (!optionalMeeting.isPresent()) throw new AccessDeniedException(ErrorCode.MEETING_NOT_FOUND);

        // 채팅 리스트를 제외한 모든 정보 가져오기
        Meeting meeting = optionalMeeting.get();
        ChattingResponseDto chattingResponseDto = new ChattingResponseDto(meeting);
        return chattingResponseDto;
    }

    /**
     * @param memberId 멤버ID
     * @return 입장한 멤버의 정보를 조회한다.
     * **/
    public ChattingMemberDto getChattingMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) return new ChattingMemberDto(optionalMember.get());
        else throw new AccessDeniedException(ErrorCode.MEMBER_NOT_FOUND);
    }

    /**
     * @param meetingId 미팅ID
     * @param memberId 멤버ID
     * @param lastNumber 최근 조회 최소 인덱스
     * @return 기록 30개를 조회한다.
     * **/
    public ChattingListDto getChattingLog(Long meetingId, Long memberId, Long lastNumber) {

        // redis에서 meetingId의 채팅 정보를 가져온다.
        ListOperations<Long, ChattingDto> listOperations = redisTemplateDto.opsForList();

        // 꺼내야 하는 개수
        int default_num = 20;
        // meetingId에 해당하는 채팅의 크기 확인
        Long redisSize = listOperations.size(meetingId);
        // MySQL에 저장된 값 가져오기
        Long mysqlSize = chattingRepository.countByMeeting_Id(meetingId);
        // 전체 개수
        Long length = redisSize + mysqlSize;
        if (lastNumber < 0) lastNumber = length;

        // chattingDtoList 생성
        List<ChattingDetailDto> chattingDetailDtoList = new ArrayList<>();
        
        // Redis에 저장된 값을 가져와야 하는 경우
        if (lastNumber > mysqlSize) {

            // Redis Index
            Long redisIdx = lastNumber - mysqlSize;

            // Redis에 저장된 값으로 충분한 경우
            if (redisIdx >= default_num) {
                chattingDetailDtoList.addAll(listOperations.range(meetingId, redisIdx - default_num + 1, redisIdx).stream()
                        .map(m -> new ChattingDetailDto(m, memberId, meetingId))
                        .sorted(Comparator.comparing(ChattingDetailDto::getChattingTime)
                        .reversed()).collect(Collectors.toList()));
                return new ChattingListDto(chattingDetailDtoList, redisIdx - default_num);
            }
            
            // Redis에 저장된 값으로 충분하지 않나, Redis에 저장된 값도 가져와야 하는 경우
            else {
                chattingDetailDtoList.addAll(listOperations.range(meetingId, 0, redisIdx).stream()
                        .map(m -> new ChattingDetailDto(m, memberId, meetingId))
                        .sorted(Comparator.comparing(ChattingDetailDto::getChattingTime)
                        .reversed()).collect(Collectors.toList()));
                lastNumber -= redisIdx;
            }
        }

        // MySQL에 저장된 값 가져오기
        if (lastNumber <= mysqlSize) {

            // lastNumber 80일 때는 0, lastNumber 70일 때는 0, lastNumber 60일 때는 1
            int page = (int) ((mysqlSize - lastNumber) / default_num);

            // MySQL에서 Pagination으로 page번째 값 가져오기
            PageRequest pageRequest = PageRequest.of(page, default_num);
            Page<Chatting> chattingPage = chattingRepository.findAllByMeeting_IdOrderByChattingTimeDesc(meetingId, pageRequest);
            
            // MySQL에서 가져온 리스트
            List<ChattingDetailDto> chattingDetailDtos = chattingPage.getContent().stream().map(m -> new ChattingDetailDto(m, memberId)).collect(Collectors.toList());

            // 나눠 떨어지면 전체 반환, 그 외에는 이미 보여진 부분은 제외하고 잘라서 보여주기
            int rest = (int) ((mysqlSize - lastNumber) % default_num);
            if (rest != 0) chattingDetailDtos = chattingDetailDtos.subList(rest, chattingDetailDtos.size());
            chattingDetailDtoList.addAll(chattingDetailDtos);
            lastNumber -= chattingDetailDtos.size();
        }

        return new ChattingListDto(chattingDetailDtoList, lastNumber);
    }
}