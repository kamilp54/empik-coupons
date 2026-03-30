package com.interview.empik.coupons.validator;

import com.interview.empik.coupons.exception.CouponValidationException;
import com.interview.empik.coupons.model.Coupon;
import com.interview.empik.coupons.model.CreateCoupon;
import com.interview.empik.coupons.model.UseCoupon;
import com.interview.empik.coupons.repository.CouponRepository;
import com.interview.empik.coupons.repository.UserCouponUsageRepository;
import org.springframework.stereotype.Component;

@Component
public class CouponValidator {

    private final UserCouponUsageRepository userCouponUsageRepository;
    private final CouponRepository couponRepository;

    public CouponValidator(UserCouponUsageRepository userCouponUsageRepository, CouponRepository couponRepository) {
        this.userCouponUsageRepository = userCouponUsageRepository;
        this.couponRepository = couponRepository;
    }

    public void validateUseCoupon(UseCoupon useCoupon, Coupon coupon, String country) throws CouponValidationException {
        if (coupon == null) {
            throw new CouponValidationException(4001, "The coupon does not exist");
        }
        if (!coupon.getCountry().equals(country)) {
            throw new CouponValidationException(4002, "The coupon " + coupon.getCode() + " can only be used in " + coupon.getCountry());
        }
        if (userCouponUsageRepository.checkIfUserUsedCoupon(coupon.getCouponId(), useCoupon.getUser())) {
            throw new CouponValidationException(4003, "The user has already used this coupon");
        }
        if (coupon.getMaxUsages() < coupon.getCurrentUsages() + 1) {
            throw new CouponValidationException(4004, "The maximum number of coupon uses has been reached");
        }
    }

    public void validateCreateCoupon(CreateCoupon createCoupon) throws CouponValidationException {
        if (couponRepository.checkIfCouponExists(createCoupon.getCode())) {
            throw new CouponValidationException(4005, "The coupon " + createCoupon.getCode() + " already exists");
        }
    }
}
