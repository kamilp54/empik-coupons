package com.interview.empik.coupons.service;

import com.interview.empik.coupons.repository.UserCouponUsageRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCouponUsageService {

    private final UserCouponUsageRepository userCouponUsageRepository;

    public UserCouponUsageService(UserCouponUsageRepository userCouponUsageRepository) {
        this.userCouponUsageRepository = userCouponUsageRepository;
    }

    public void addUserCouponUsage(long couponId, String userName) {
        userCouponUsageRepository.addUserCouponUsage(couponId, userName);
    }
}
