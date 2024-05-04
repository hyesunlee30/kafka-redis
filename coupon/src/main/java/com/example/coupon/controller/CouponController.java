package com.example.coupon.controller;


import com.example.coupon.dto.MemberDto;
import com.example.coupon.service.ApplyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CouponController
{
    private final ApplyService applyService;

    public CouponController(ApplyService applyService)
    {
        this.applyService = applyService;
    }

    @PostMapping("/apply")
    private void couponApply(MemberDto memberDto)
    {
        applyService.apply(memberDto.getMemberId());
    }
}
