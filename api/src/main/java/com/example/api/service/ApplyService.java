package com.example.api.service;

import com.example.api.domain.ApiCoupon;
import com.example.api.repository.ApiCouponCountRepository;
import com.example.api.repository.ApiCouponRepository;
import com.example.member.controller.MemberDto;
import org.springframework.stereotype.Service;

@Service
public class ApplyService
{
    private final ApiCouponRepository apiCouponRepository;
    private final ApiCouponCountRepository apiCouponCountRepository;


    public ApplyService(ApiCouponRepository apiCouponRepository, ApiCouponCountRepository apiCouponCountRepository)
    {
        this.apiCouponRepository = apiCouponRepository;
        this.apiCouponCountRepository = apiCouponCountRepository;
    }

//   public void apply(Long userId){ //DB로만 쿠폰 생성 했을 때
//        long count = apiCouponRepository.count();
//
//        if(count > 100){
//            return;
//        }
//
//       apiCouponRepository.save(new ApiCoupon(userId));
//    }
     public void apply(Long userId){//Redis의 incr 명령어로 count
         Long count = apiCouponCountRepository.increment();


         if(count > 100){
            return;
        }
         System.out.println("count "+count);
         apiCouponRepository.save(new ApiCoupon(userId));
         //apiCouponCreateProducer.create(userId);
    }

    public void apply(MemberDto memberDto)
    {

    }
}
