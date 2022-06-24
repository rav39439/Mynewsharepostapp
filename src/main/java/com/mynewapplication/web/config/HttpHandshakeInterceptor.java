package com.mynewapplication.web.config;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;



public class HttpHandshakeInterceptor extends DefaultHandshakeHandler {


	 private final Logger LOG = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        
        System.out.println("the new user"+request);
        LOG.info("User with ID '{}' opened the page", request);

        return new UserPrincipal(randomId);
    }
}
