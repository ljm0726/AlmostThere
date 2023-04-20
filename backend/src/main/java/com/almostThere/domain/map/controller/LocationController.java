package com.almostThere.domain.map.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

    @MessageMapping("/locShare/{meetingId}")
    @SendTo("/topic/{meetingId}")
    public String customerRequestMessage(@DestinationVariable long meetingId, String message){

        return message;
    }

}
