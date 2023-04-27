package com.almostThere.domain.map.Service;

import com.almostThere.domain.map.entity.UserLocation;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final MeetingRepository meetingRepository;
    @Qualifier("redisTemplateForLocation")
    private final RedisTemplate redisTemplateForLocation;

    //{"member":{"memberId":1,"memberNickname":"김싸피","memberLatLng":[37.5016923,127.0393868]}}


    public List<UserLocation> getAllMemberLocationsByMeetingId(long meetingId){
        ObjectMapper objectMapper = new ObjectMapper();
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_NOT_FOUND));

        List<MeetingMember> meetingMembers = meeting.getMeetingMembers();
        List<UserLocation> userLocations = new ArrayList<>();

        for(MeetingMember meetingMember : meetingMembers){
            String memberId = String.valueOf(meetingMember.getMember().getId());
             LinkedHashMap result = (LinkedHashMap)redisTemplateForLocation.opsForValue().get(memberId);
            System.out.println(result.get("memberId"));
            UserLocation userLocation = new UserLocation((long)result.get("memberId"), (String)result.get("memberNickName"),(double[])result.get("memberLatLng"));
            System.out.println(userLocation);
//            userLocations.add(userLocation);
        }

        return userLocations;
    }
}
