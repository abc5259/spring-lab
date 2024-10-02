package com.example.demo.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventServiceV2Test {

    @Autowired
    private EventServiceV2 eventServiceV2;

    @Test
    void test() {
        System.out.println(Thread.currentThread().getName());
        eventServiceV2.sendEmail("address", "content");
    }
}