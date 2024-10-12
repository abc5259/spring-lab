package com.example.demo.sse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class SseEmitters {

    private static final AtomicLong counter = new AtomicLong();

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        log.info("emitter list: {}", emitters);
        /**
         * 타임아웃이 발생하면 브라우저에서 재연결 요청을 보내는데,
         * 이때 새로운 Emitter 객체를 다시 생성하기 때문에(SseController의 connect()메서드 참조)
         * 기존의 Emitter를 제거해주어야 한다.
         * 주의할 점은 이 콜백이 SseEmitter를 관리하는 다른 스레드에서 실행된다는 것이다
         * 따라서 thread-safe한 자료구조를 사용하지 않으면 ConcurrnetModificationException이 발생할 수 있다
         * 여기서는 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용
         */
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void count() {
        long count = counter.incrementAndGet();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(count));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
