package com.example.coupon.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private String couponName;
    private LocalDateTime createDate;

    public Coupon(Long memberId)
    {
        this.memberId = memberId;
    }

    public Coupon(Long memberId, String couponName, LocalDateTime createDate)
    {
        this.memberId = memberId;
        this.couponName = couponName;
        this.createDate = createDate;
    }


}
