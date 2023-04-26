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
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.data.redis.core.RedisTemplate;

@Controller
public class LocationController {


    @MessageMapping("/locShare/{meetingId}")
    @SendTo("/topic/{meetingId}")
    public String customerRequestMessage(@DestinationVariable long meetingId, String message){
        System.out.println("?????????????");
        return message;
    }


//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    private final RedisTemplate redisTemplate;
//    @MessageMapping("/locShare/{meetingId}")
//    @SendTo("/topic/{meetingId}")
//    public String customerRequestMessage(@DestinationVariable long meetingId, String message){
//        System.out.println("????????????/");
//        try {
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
//
//            // Extract member object from JSONObject
//            JSONObject memberObject = (JSONObject) jsonObject.get("member");
//
//            // Extract data from memberObject
//            Long memberId = (Long) memberObject.get("memberId");
//            String memberNickname = (String) memberObject.get("memberNickname");
//            JSONArray memberLatLng = (JSONArray) memberObject.get("memberLatLng");
//            Double memberLatitude = (Double) memberLatLng.get(0);
//            Double memberLongitude = (Double) memberLatLng.get(1);
//
//            // Print the extracted data
//            System.out.println("Member ID: " + memberId);
//            System.out.println("Member Nickname: " + memberNickname);
//            System.out.println("Member Latitude: " + memberLatitude);
//            System.out.println("Member Longitude: " + memberLongitude);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
////        // Save an instance of MyClass to Redis
////        UserLocation userLocation = new UserLocation(, 123);
////        redisTemplate.opsForValue().set("myKey", myObject);
//
//        return "hi";
//    }

}
