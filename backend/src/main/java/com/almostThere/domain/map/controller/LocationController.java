package com.almostThere.domain.map.controller;

import com.almostThere.domain.map.entity.UserLocation;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
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
    private final RedisTemplate redisTemplateForLocation;
    @MessageMapping("/locShare/{meetingId}")
    @SendTo("/topic/{meetingId}")
    public String customerRequestMessage(@DestinationVariable long meetingId, String message) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
        JSONObject memberObject = (JSONObject) jsonObject.get("member");

        String memberId =  String.valueOf(memberObject.get("memberId"));
        String memberNickname = (String) memberObject.get("memberNickname");
        JSONArray memberLatLng = (JSONArray) memberObject.get("memberLatLng");
        Double memberLatitude = (Double) memberLatLng.get(0);
        Double memberLongitude = (Double) memberLatLng.get(1);

        UserLocation userLocation = new UserLocation(memberNickname, memberLatitude,memberLongitude);

        //RedisTemplate의 opsFor* 메소드들은 특정 컬렉션의 커맨드(Operation)을 호출할 수 있는 기능을 모아둔 *Operations 인터페이스를 반환
        redisTemplateForLocation.opsForValue().set(memberId, userLocation);

        return message;
    }

}
