package com.almostThere.domain.map.controller;

import com.almostThere.domain.map.Service.LocationService;
import com.almostThere.domain.map.entity.UserLocation;
import com.almostThere.global.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.data.redis.core.RedisTemplate;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Qualifier("redisTemplateForLocation")
    private final RedisTemplate<String, UserLocation> redisTemplateForLocation;
    private final LocationService locationService;

    @MessageMapping("/locShare")
    public void saveLocation(String message) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
        JSONObject memberObject = (JSONObject) jsonObject.get("member");

        String memberId =  String.valueOf(memberObject.get("memberId"));
        String memberNickname = (String) memberObject.get("memberNickname");
        JSONArray memberLatLngJson = (JSONArray) memberObject.get("memberLatLng");
        double lat = (double) memberLatLngJson.get(0);
        double lng = (double) memberLatLngJson.get(1);

        UserLocation userLocation = new UserLocation(Long.parseLong(memberId), memberNickname, new double[] {lat, lng});
        //RedisTemplate의 opsFor* 메소드들은 특정 컬렉션의 커맨드(Operation)을 호출할 수 있는 기능을 모아둔 *Operations 인터페이스를 반환
        redisTemplateForLocation.opsForValue().set(memberId, userLocation);

    }
    /*
        유저가 위치값을 보낸 적이 없어서 redis에 위치 정보가 저장되어 있지 않을 수가 있나?
        위치 정보를 언제부터 전송하지.? 자신이 속한 모임 중 약속시간이 3시간 이내로 남은 모임이 있으면 전송
        그럼 만약 그런 모임이 있는데 로그인을 안 했으면? 전송을 못 하겠네?
        전송을 안 해서 저장이 안 되어 있으면 그 사람의 위치는 모르겠네?
        그럼 그때는 출발위치로 찍어 놓기? 출발위치도 없으면 안 찍고
     */
    @MessageMapping("/locShare/meetingId/{meetingId}/memberId/{memberId}")
    @SendTo("/topic/{memberId}")
    public List<UserLocation> getAllUsersLocations(@DestinationVariable long meetingId, @DestinationVariable long memberId, String message) throws ParseException {

        System.out.println("meetingId: "+meetingId);
        System.out.println("memberId: "+memberId);

        return locationService.getAllMemberLocationsByMeetingId(meetingId);
    }

}
