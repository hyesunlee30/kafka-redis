package com.coupon.api.service;


import com.coupon.api.domain.Coupon;
import com.coupon.api.repository.CouponCountRepository;
import com.coupon.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }

    public void apply(Long userId) {
        //레이스 컨디션 해결하기 위해
        //java synchronized를 생각해 볼 수 있겠지만
        //서버가 여러대가 되면 다시 레이스 컨디션이 다시 발생
        //또 다른 방법으로는 MySql, Redis를 활용한 락을 구현해서
        //쿠폰 개수에 대한 정합성인데 락을 활용하여 구현한다면
        //발급된 쿠폰 개수를 가져오는 것부터 쿠폰을 생성할 때까지
        //락을 걸어야 한다. 결국 성능에 불이익이 있을 수 있다.

        //예를 들어서 저장하는 로직이 2초가 걸리게 된다면 락은 2초 뒤에 풀리게 되고,
        //사용자들은 그만큼 기다려야 한다는 뜻

        //쿠폰 개수에 대한 정합성만 관리하면 됨.

        //redis incr 명령어 키에 대한 밸류를 1씩 증가시키는 명령어
        //redis는 싱글스레드 기반으로 동작하여 이 명령어를 사용하여
        //발급된 쿠폰 개수를 제어한다면 성능도 빠르며 데이터 정합성도 지킬 수 있다



        //long count = couponRepository.count();

        //쿠폰 발급 전에 발급된 쿠폰의 개수를 증가시키고
        Long count = couponCountRepository.increment();
        //발급된 쿠폰의 개수가 100개보다 많으면 발급되지 않도록
        if(count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));

    }
}
