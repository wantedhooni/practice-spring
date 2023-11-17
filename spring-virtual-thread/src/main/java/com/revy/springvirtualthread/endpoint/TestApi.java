package com.revy.springvirtualthread.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Revy on 2023.11.16
 */

@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class TestApi {

    private final ApplicationEventPublisher publisher;


    @GetMapping("/virtual")
    public String test() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        Thread.sleep(4000);
        log.info("threadName: {}", Thread.currentThread().getName());
        return threadName;
    }
}
