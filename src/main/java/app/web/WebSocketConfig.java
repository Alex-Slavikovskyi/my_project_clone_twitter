package app.web;

import app.enums.TokenType;
import app.exceptions.authError.JwtAuthenticationException;
import app.security.JwtUserDetails;
import app.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    registration.setMessageSizeLimit(128 * 1024);
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic/chats", "/topic/notifications");
    registry.setApplicationDestinationPrefixes("/api");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat-ws").setAllowedOriginPatterns("http://localhost:3000", "https://final-step-fe-8-fs-2.vercel.app",
      "http://localhost:3000/**", "https://final-step-fe-8-fs-2.vercel.app/**"); //TODO: need to change on deploy

    registry.addEndpoint("/notifications-ws").setAllowedOriginPatterns("final-step-fe2fs8tw.herokuapp.com",
      "final-step-fe2fs8tw.herokuapp.com/**", "http://localhost:8080", "http://localhost:8080/**",
      "https://final-step-fe-8-fs-2.vercel.app", "https://final-step-fe-8-fs-2.vercel.app/**"); //TODO: need to change on deploy
  }

  @Override
  public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
    DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
    resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(objectMapper);
    converter.setContentTypeResolver(resolver);
    messageConverters.add(converter);
    return false;
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      @Order(Ordered.HIGHEST_PRECEDENCE + 99)
      public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        String origin = accessor.getFirstNativeHeader("Origin");

        if (accessor != null && accessor.getCommand() != null && origin != null && !origin.startsWith("http://localhost:8080")) {
          if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = jwtTokenService.extractTokenFromHeader(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")))
              .orElseThrow(() -> new JwtAuthenticationException("Token not found!"));

            if (jwtTokenService.validateToken(token, TokenType.ACCESS)) {
              Authentication user = jwtTokenService.extractClaimsFromToken(token, TokenType.ACCESS)
                .flatMap(jwtTokenService::extractIdFromClaims)
                .map(JwtUserDetails::new)
                .map(jwtUserDetails -> new UsernamePasswordAuthenticationToken(jwtUserDetails, "", jwtUserDetails.getAuthorities()))
                .orElseThrow(() -> new JwtAuthenticationException("Authentication failed"));
              accessor.setUser(user);
              //JwtUserDetails jwtUser = (JwtUserDetails) user.getDetails();
              //accessor.getSessionAttributes().put("userId", jwtUser.getId());
              accessor.getSessionAttributes()
                .put("userId", jwtTokenService.extractIdFromClaims(jwtTokenService.extractClaimsFromToken(token, TokenType.ACCESS).get()).get());
            } else {
              throw new JwtAuthenticationException("Token is not valid");
            }
          }
        }
        return message;
      }
    });
  }


  //  @Override
//  public void configureClientInboundChannel(ChannelRegistration registration) {
//    registration.interceptors(new ChannelInterceptor() {
//      @Override
//      @Order(Ordered.HIGHEST_PRECEDENCE + 99)
//      public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//        StompHeaderAccessor accessor =
//          MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//        if (accessor != null && accessor.getCommand() != null) {
//          if (StompCommand.CONNECT.equals(accessor.getCommand()) ||
//            StompCommand.SEND.equals(accessor.getCommand()) ||
//            StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
//
//            String token = jwtTokenService.extractTokenFromHeader(accessor.getFirstNativeHeader("Authorization"))
//              .orElseThrow(() -> new JwtAuthenticationException("Token not found!"));
//
//            if (jwtTokenService.validateToken(token, TokenType.ACCESS)) {
//              Authentication user = jwtTokenService.extractClaimsFromToken(token, TokenType.ACCESS)
//                .flatMap(jwtTokenService::extractIdFromClaims)
//                .map(JwtUserDetails::new)
//                .map(jwtUserDetails -> new UsernamePasswordAuthenticationToken(jwtUserDetails, "", jwtUserDetails.getAuthorities()))
//                .orElseThrow(() -> new JwtAuthenticationException("Authentication failed"));
//              accessor.setUser(user);
//            } else {
//              throw new JwtAuthenticationException("Token is not valid");
//            }
//          }
//        }
//        return message;
//      }
//    });
}
