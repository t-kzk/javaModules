package org.kzk.demo.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyEventListener {
    @EventListener
    public void handleUserCreated(String event) {
        System.out.println("Received user created event for: " + event);
    }
}
