package com.example.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.coupon.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class CouponServiceTest
{
    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 한번에_한_쿠폰만_발급()
    {
        applyService.apply(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 여러명이_한번에_응모() throws InterruptedException
    {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 모든 Thread들의 수행이 끝날때 까지 대기
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            long userId = i;
            executorService.submit(()->{
                try
                {
                    applyService.apply(userId);
                }finally
                {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        Thread.sleep(10000);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(100);

    }


}