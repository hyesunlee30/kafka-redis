package com.example.coupon.service;



import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.Member;
import com.example.coupon.domain.MemberCoupon;
import com.example.coupon.dto.CouponStatus;
import com.example.coupon.dto.KafkaLimitedCoupon;
import com.example.coupon.repository.CouponCountRepository;
import com.example.coupon.repository.CouponRepository;
import com.example.coupon.repository.MemberCouponRepository;
import com.example.coupon.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplyService
{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, MemberRepository memberRepository, MemberCouponRepository memberCouponRepository)
    {

        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.memberRepository = memberRepository;
        this.memberCouponRepository = memberCouponRepository;
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
         Long count = couponCountRepository.increment();


         if(count > 100){
            return;
        }
         System.out.println("count "+count);
         couponRepository.save(new Coupon(userId));
         //apiCouponCreateProducer.create(userId);
    }

    @Transactional
    public void limitedCouponReceive(String data) throws JsonProcessingException {

        KafkaLimitedCoupon limitedCoupon = objectMapper.readValue(data, KafkaLimitedCoupon.class);

        Optional<Member> member = memberRepository.findById(limitedCoupon.getUserId());
        Optional<Coupon> coupon = couponRepository.findById(limitedCoupon.getCouponId());


        if(member.isPresent() && coupon.isPresent()) {
            List<MemberCoupon> receiveCouponList = memberCouponRepository.findByCouponIdAndMemberId(limitedCoupon.getCouponId(), limitedCoupon.getUserId());

            if(receiveCouponList.isEmpty()) {

                MemberCoupon memberCoupon = MemberCoupon.builder().coupon(coupon.get()).member(member.get()).couponStatus(CouponStatus.ISSUANCE).build();
                memberCouponRepository.save(memberCoupon);
            }
        }

    }


}
