package com.revy.example.db_master_slave.controller;

import com.revy.example.db_master_slave.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/query")
    public void query(){
        testService.testQuery();
    }

    @GetMapping("/read-only-query")
    public void readOnlyQuery(){
        testService.testReadOnlyQuery();
    }
}
