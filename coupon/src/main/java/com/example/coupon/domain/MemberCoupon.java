package com.example.coupon.domain;

import com.example.coupon.dto.CouponStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    @JsonIgnore
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;
}
