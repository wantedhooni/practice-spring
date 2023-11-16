package com.revy.springvirtualthread.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Revy on 2023.11.16
 */
@EnableAsync
@Configuration
public class AsyncConfig {

    /**
     * 비동기 서비스 수행을 위한 Executor Bean 설정
     * @return Executor
     */
    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("Async-exec-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(newVirtualThreadPerTaskExecutor("V-exec-"));
        };
    }

    private ExecutorService newVirtualThreadPerTaskExecutor(String threadName) {
        // 쓰레드 이름 지정을 위해 newVirtualThreadPerTaskExecutor 미사용
        ThreadFactory factory = Thread.ofVirtual()
                .name(threadName, 0)
                .factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }
}
