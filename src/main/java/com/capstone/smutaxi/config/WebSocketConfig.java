package com.capstone.smutaxi.config;

import com.capstone.smutaxi.utils.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub"); //메시지 브로커가 /sub으로 시작하는 주소를 구독한 Subscriber들에게 메시지를 전달
        config.setApplicationDestinationPrefixes("/pub"); //클라이언트가 서버로 메시지를 발송할 수 있는 경로의 prefix를 지정한다.
    }

    /**
     * @param registry
     * 소켓에 연결하기 위한 엔드 포인트를 지정해준다. 이 때 CORS를 피하기 위해 AllowedOriginPatterns를 *으로 지정해줬다.(실무에서는 정확한 도메인 지정 필요)
     *
     * CORS는?
     * 브라우저에서는 보안적인 이유로 cross-origin HTTP 요청들을 제한합니다. 그래서 cross-origin 요청을 하려면 서버의 동의가 필요합니다.
     * 만약 서버가 동의한다면 브라우저에서는 요청을 허락하고, 동의하지 않는다면 브라우저에서 거절합니다.
     * 이러한 허락을 구하고 거절하는 메커니즘을 HTTP-header를 이용해서 가능한데, 이를 CORS(Cross-Origin Resource Sharing)라고 부릅니다.
     * 그래서 브라우저에서 cross-origin 요청을 안전하게 할 수 있도록 하는 메커니즘입니다.
     *
     * 그러면 cross-origin은?
     * cross-origin이란 다음 중 한 가지라도 다른 경우를 말합니다.
     *
     * 프로토콜 - http와 https는 프로토콜이 다르다.
     * 도메인 - domain.com과 other-domain.com은 다르다.
     * 포트 번호 - 8080포트와 3000포트는 다르다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*").withSockJS();//sockJs 쓰면 연결할때 /websocket 추가
    }

    /**
     * 클라이언트로부터 들어오는 STOMP 메시지를 처리하는 인바운드 채널에 인터셉터를 등록하는 역할
     * 인바운드채널: client로부터 서버로 수신된 메세지 처리채널
     * 인터셉터: 메시지 전송 또는 메시지 수신 과정에서 실행되는 코드로, 메시지를 가로채고 처리
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
