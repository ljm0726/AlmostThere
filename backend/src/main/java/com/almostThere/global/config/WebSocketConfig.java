package com.almostThere.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    @Override
//    // 클라이언트가 메시지를 구독할 endpoint를 정의합니다.
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//
//        config.enableSimpleBroker("/send");
//    }

//    @Override
    // connection을 맺을때 CORS 허용합니다.
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/")
//                // 스프링 5.3, 스프링부트 2.4 버전 부터 allowCredentials이 true인 경우 setAllowedOrigins 메서드에서
//                // 와일드 카드 `'*'`을 사용하실 수 없습니다.
//                //  	.setAllowedOrigins("*")
//                .setAllowedOriginPatterns("*")
//                .withSockJS();
//    }

    private final StompHandler stompHandler;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket") // 클라이언트에서 서버로 소켓 연결하는 endPoint 지정
            .setAllowedOriginPatterns("*") // CORS 설정
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/send"); // 메시지 수신을 위한 서버 연결 주소 prefix
        registry.setApplicationDestinationPrefixes("/message"); // 메시지 발신을 위한 서버 연결 주소 prefix
    }

    // Socket Controller에 요청이 전달되기 전 처리할 로직을 사용하는 Interceptor 등록 (Config의 StompHandler)
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(stompHandler);
    }

}