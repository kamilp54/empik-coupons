package com.interview.empik.coupons.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Coupon {
    private Long couponId;
    private String code;
    private Timestamp createdDate;
    private int maxUsages;
    private int currentUsages;
    private String country;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public int getMaxUsages() {
        return maxUsages;
    }

    public void setMaxUsages(int maxUsages) {
        this.maxUsages = maxUsages;
    }

    public int getCurrentUsages() {
        return currentUsages;
    }

    public void setCurrentUsages(int currentUsages) {
        this.currentUsages = currentUsages;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return maxUsages == coupon.maxUsages && currentUsages == coupon.currentUsages && Objects.equals(couponId, coupon.couponId) && Objects.equals(code, coupon.code) && Objects.equals(createdDate, coupon.createdDate) && Objects.equals(country, coupon.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponId, code, createdDate, maxUsages, currentUsages, country);
    }
}
