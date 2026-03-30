package com.interview.empik.coupons.repository;

import com.interview.empik.coupons.model.Coupon;
import com.interview.empik.coupons.model.CreateCoupon;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CouponRepository {

    @Select("SELECT COUPON_ID AS couponId, CODE, CREATED_DATE AS createdDate, MAX_USAGES as maxUsages, CURRENT_USAGES AS currentUsages, COUNTRY " +
            "FROM COUPONS WHERE CODE = #{code} FOR UPDATE")
    Coupon findCouponByCode(String code);

    @Insert("INSERT INTO COUPONS(CODE, MAX_USAGES, COUNTRY) VALUES (#{code}, #{maxUsages}, #{country})")
    void createCoupon(CreateCoupon coupon);

    @Update("UPDATE COUPONS SET CURRENT_USAGES = CURRENT_USAGES + 1 WHERE COUPON_ID=#{couponId} ")
    void updateUsage(Long couponId);

    @Select("SELECT EXISTS(SELECT 1 FROM COUPONS WHERE CODE = #{code})")
    boolean checkIfCouponExists(String code);
}
