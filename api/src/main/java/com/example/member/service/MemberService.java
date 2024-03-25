package com.example.member.service;

import com.example.coupon.repository.CouponCountRepository;
import com.example.member.controller.MemberDto;
import com.example.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService
{
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository, CouponCountRepository couponCountRepository)
    {
        this.memberRepository = memberRepository;
    }

    public void login(MemberDto memberDto)
    {
    }
}
