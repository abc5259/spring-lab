package com.example.demo.event;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Spring 컨테이너는 EmailService가 ApplicationEventPublisherAware를 구현하는 것을 감지하고
 * 자동으로 setApplicationEventPublisher()를 호출
 * 실제로 전달되는 매개 변수는 Spring 컨테이너 자체이다.
 */
@Service
public class EventService implements ApplicationEventPublisherAware {

    private List<String> blockedList;
    private ApplicationEventPublisher publisher;

    public void setBlockedList(List<String> blockedList) {
        this.blockedList = blockedList;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        System.out.println("publisher = " + publisher);
        this.publisher = publisher;
    }

    public void sendEmail(String address, String content) {
        if (blockedList.contains(address)) {
            publisher.publishEvent(new BlockedListEvent(this, address, content));
            return;
        }
        // send email...
    }
}
