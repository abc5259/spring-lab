package com.example.demo.event;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Slf4j
public class BlockedListNotifierV2 {

    /**
     * spring 4.2부터 @EventListener 을 사용하여 관리되는 빈의 모든 메서드에 이벤트 리스너를 등록할 수 있음
     */
    @EventListener
    public void onApplicationEvent(BlockedListEvent event) {
        // notify appropriate parties via notificationAddress...
        String value = MDC.get("hello");
        System.out.println("value = " + value);
    }

    /**
     * 여러 이벤트를 수신할 수 있음
     */
    @EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
    public void handleContextStart() {
        // ...
    }

    /**
     * SpEL 표현식을 사용해서 이벤트를 수신할 수 있음
     */
    @EventListener(condition = "#blEvent.content == 'my-event'")
    public void processBlockedListEvent(BlockedListEvent blEvent) {
        // notify appropriate parties via notificationAddress...
    }

    /**
     * 이벤트를 처리한 결과로 이벤트를 발행하고 싶다면 다음과 같이 메서드 시그니처에 발행하기 원하는 이벤트를 두면됨
     */
    @EventListener
    public ListUpdateEvent handleBlockedListEvent(BlockedListEvent event) {
        // notify appropriate parties via notificationAddress and
        // then publish a ListUpdateEvent...
        return new ListUpdateEvent();
    }

    /**
     * 이벤트를 비동기적으로 처리하도록 하려면 @Async를 사용하면된다.
     * 비동기 이벤트 사용시 유의사항
     *  (1) 이벤트 리스너가 예외를 던지면 이벤트를 발행한 class까지 전파되지 않는다.
     *      AsyncUncaughtExceptionHandler를 사용해서 비동기에서 던져진 예외에 관리가 가능
     *  (2) 비동기 이벤트 리스너 메서드는 값을 반환하여 후속 이벤트를 게시할 수 없다.
     *      이벤트를 발행하고 싶다면 ApplicationEventPublisher을 주입해서 사용하면된다.
     *  (3) ThreadLocal과 로깅 컨텍스트는 이벤트 처리를 위해 기본적으로 전파되지 않는다.
     */
    @EventListener
    @Async
    public void processBlockedListEventAsync(BlockedListEvent event) {
        // BlockedListEvent is processed in a separate thread
        log.info("TransactionName: {}", TransactionSynchronizationManager.getCurrentTransactionName());
        log.info("TransactionActive: {}", TransactionSynchronizationManager.isActualTransactionActive());
    }
}
