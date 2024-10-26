package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;

@Component
public class PasswordEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public PasswordEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;                
    }
    
    public void publishPasswordEvent(User user, String requestUrl) {
        applicationEventPublisher.publishEvent(new PasswordEvent(this, user, requestUrl));
    }
}
