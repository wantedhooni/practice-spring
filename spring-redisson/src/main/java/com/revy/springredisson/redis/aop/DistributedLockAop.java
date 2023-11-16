package com.revy.springredisson.redis.aop;


import com.revy.springredisson.redis.aop.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Revy on 2023.11.13
 * 분산락 수행 AOP
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DistributedLockAop {

    private final RedissonClient redissonClient;
    private final RedisLockTransaction redisLockTransaction;

    @Around("@annotation(com.revy.springredisson.redis.aop.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = createKey(distributedLock.prefix(), distributedLock.key(), signature.getParameterNames(), joinPoint.getArgs());
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean isLocked = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!isLocked) {
                return false;
            }
            return redisLockTransaction.proceed(joinPoint);
        } finally {
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }

    /**
     * redis Lock에 사용할 key값을 생성한다.
     *
     * @param prefix
     * @param key
     * @param parameterNames
     * @param args
     * @return - reids lock key
     */
    private String createKey(String prefix, String key, String[] parameterNames, Object[] args) {
        StringBuilder stringBuilder = new StringBuilder(prefix)
                .append(":").append(key).append(":");

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(key)) {
                stringBuilder.append(args[i].toString());
                break;
            }
        }
        return stringBuilder.toString();
    }


}
