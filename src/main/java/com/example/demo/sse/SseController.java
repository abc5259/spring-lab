package com.example.demo.sse;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
public class SseController {

    private final SseEmitters sseEmitters;

    public SseController(SseEmitters sseEmitters) {
        this.sseEmitters = sseEmitters;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        /**
         * SseEmitter는 생성자를 통해서 만료시간을 설정할 수 있음
         * 스프링 부트의 내장 톰캣을 사용하면 30초로 설정
         * 시간이 되면 브라우저에서 자동으로 서버에 재연결 요청을 보냄
         */
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        /**
         * 생성된 SseEmitter 객체는 향후 이벤트가 발생했을 때, 해당 클라이언트로 이벤트를 전송하기 위해 사용되므로
         * 서버에서 저장하고 있어야한다.
         */
        sseEmitters.add(emitter);
        try {
            /**
             * Emitter를 생성하고 나서 만료 시간까지 아무런 데이터도 보내지 않으면
             * 재연결 요청시 503 Service Unavailable 에러가 발생할 수 있다.
             * 따라서 처음 SSE 연결 시 더미 데이터를 전달해주는 것이 안전하다.
             */
            emitter.send(SseEmitter.event()
                    .name("connect") // 해당 이벤트의 이름 지정
                    .data("connected!")); // 503 에러 방지를 위한 더미 데이터
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/count")
    public ResponseEntity<Void> count() {
        sseEmitters.count();
        return ResponseEntity.ok().build();
    }

}
