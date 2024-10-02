package com.example.demo.event;

import ch.qos.logback.classic.turbo.MDCFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceV2 {

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void sendEmail(String address, String content) {
        log.info("TransactionName: {}", TransactionSynchronizationManager.getCurrentTransactionName());
        log.info("TransactionActive: {}", TransactionSynchronizationManager.isActualTransactionActive());
        publisher.publishEvent(new BlockedListEvent(this, address, content));
        // send email...
    }
}
