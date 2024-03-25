package com.example.api.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.api.repository.ApiCouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiCouponServiceTest
{
    @Autowired
    private ApplyService applyService;

    @Autowired
    private ApiCouponRepository apiCouponRepository;

    @Test
    void 한번에_한_쿠폰만_발급()
    {
        applyService.apply(1L);

        long count = apiCouponRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 여러명이_한번에_응모() throws InterruptedException
    {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);


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

        //Consumer에서는 이벤트를 받았으나, DB에 아직 쿠폰이 모두 생성되지 않았으므로 데이터 간에 시간 텀이 발생한다.
        Thread.sleep(10000);

        long count = apiCouponRepository.count();

        assertThat(count).isEqualTo(100);

    }


}