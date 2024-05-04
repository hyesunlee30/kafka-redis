package com.example.coupon.kafka;

import com.example.coupon.service.ApplyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponKafkaEventListener {

    private final ApplyService service;


    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listen(@Payload String data) throws JsonProcessingException {
        log.info("received data : {}", data);

        service.limitedCouponReceive(data);
    }
}
