package com.interview.empik.coupons.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserCouponUsageRepository {

    @Insert("INSERT INTO USER_COUPON_USAGE(COUPON_ID, USER_NAME) VALUES (#{couponId}, #{userName})")
    void addUserCouponUsage(long couponId, String userName);

    @Select("SELECT EXISTS( SELECT 1 FROM USER_COUPON_USAGE WHERE COUPON_ID=#{couponId} AND USER_NAME=#{userName})")
    boolean checkIfUserUsedCoupon(long couponId, String userName);
}
