package com.example.coupon.repository;


import com.example.coupon.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findByCouponIdAndMemberId(Long couponId, Long memberId);
}
