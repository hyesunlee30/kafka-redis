package com.coupon.api.service;

import com.coupon.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplyServiceTest {
    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired

    @Test
    public void 한번만응모(){
         applyService.apply(1L);
         long count = couponRepository.count();

         assertThat(count).isEqualTo(1);
    }

    @Test
    public void 여러명응모() throws InterruptedException {
        //더 많은 쿠폰이 발급됨.
        // 레이스 컨디션이란 두 개 이상의 쓰레드가
        // 공유 데이터에 access을 하고 동시에 작업을 하려고 할 때 생기는 문제
        // 멀티 스레드에서만 나타나는 문제로 싱글 스레드에서는 나타나지 않음
        // 하지만 쿠폰 발급 로직 전체를 싱글 스레드로 작업을 하게 된다면 성능이 좋지 않음
        // 먼저 요청한 사람의 쿠폰 발급이 완료 되고난 후에 다음 사람의 발급이 일어나기 때문에


        int threadCount = 1000;
        //병렬작업을 간단히 할 수 있는 api
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 모든 요청이 끝날때까지 기다려야 함으로
        // 다른 스레드에서 수행하는 작업을 기다리게 하는
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(()->{
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }

            });
        }

        latch.await();

        long count = couponRepository.count();

        assertThat(count).isEqualTo(100);


    }

}