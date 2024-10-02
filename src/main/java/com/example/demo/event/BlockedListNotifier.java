package com.example.demo.event;

import org.springframework.context.ApplicationListener;


public class BlockedListNotifier implements ApplicationListener<BlockedListEvent> {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    /**
     * 기본적으로 이벤트 리스너는 이벤트를 동기식으로 수신
     * 그래서 이벤트를 발행한 쪽은 이벤트 리스너가 이벤트 처리를 완료할 때까지 기다려야된다.
     * 동기식의 장점: 이벤트를 발행한 쪽에 트랜잭션 컨텍스트가 있는 경우 해당 트랜잭션 컨텍스트 안에서 밑의 메서드를 실행한다.
     * 기본적인 이벤트 동작을 변경하고 싶으면 Spring의 ApplicationEventMulticaster 인터페이스 및 SimpleApplicationEventMulticaster 구현에 대한 javadoc을 참조
     * 비동기로 동작하게 하는 경우 ThreadLocals 및 로깅 컨텍스트가 전파되지 않는다.
     */
    public void onApplicationEvent(BlockedListEvent event) {
        // notify appropriate parties via notificationAddress...
    }
}
