package com.almostThere.domain.map.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/locShare/{meetingId}")
    @SendTo("/topic/{meetingId}")
    public void customerRequestMessage(List<Long> meetingIds, String message){


        for (Long meetingId : meetingIds) {
            // Logic to send message to each meetingId
            // You can use a messaging service or framework to send messages to multiple destinations
            // For example, you can use Spring's SimpMessagingTemplate to send messages to different topics
            // based on meetingId
            simpMessagingTemplate.convertAndSend("/topic/" + meetingId, message);
        }
//        return message;
    }

}
