package com.interview.empik.coupons.service;

import com.interview.empik.coupons.exception.CouponValidationException;
import com.interview.empik.coupons.model.Coupon;
import com.interview.empik.coupons.model.CreateCoupon;
import com.interview.empik.coupons.model.UseCoupon;
import com.interview.empik.coupons.repository.CouponRepository;
import com.interview.empik.coupons.validator.CouponValidator;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponUsageService userCouponUsageService;
    private final CouponValidator couponValidator;

    public CouponService(CouponRepository couponRepository,  UserCouponUsageService userCouponUsageService, CouponValidator couponValidator) {
        this.couponRepository = couponRepository;
        this.userCouponUsageService = userCouponUsageService;
        this.couponValidator = couponValidator;
    }

    public void useCoupon(UseCoupon useCoupon, String country) throws CouponValidationException {
        Coupon coupon = couponRepository.findCouponByCode(useCoupon.getCode());
        couponValidator.validateUseCoupon(useCoupon, coupon, country);
        couponRepository.updateUsage(coupon.getCouponId());
        userCouponUsageService.addUserCouponUsage(coupon.getCouponId(), useCoupon.getUser());
    }

    public void createCoupon(CreateCoupon createCoupon) throws CouponValidationException {
        couponValidator.validateCreateCoupon(createCoupon);
        couponRepository.createCoupon(createCoupon);
    }

}
