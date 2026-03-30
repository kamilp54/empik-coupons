package com.interview.empik.coupons.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class CreateCoupon {

    @NotBlank(message = "Coupon code cannot be blank")
    private String code;
    @Min(value = 1, message = "Max usages cannot be less than 1")
    private int maxUsages;
    @NotBlank(message = "Country name cannot be blank")
    private String country;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMaxUsages() {
        return maxUsages;
    }

    public void setMaxUsages(int maxUsages) {
        this.maxUsages = maxUsages;
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
        CreateCoupon that = (CreateCoupon) o;
        return maxUsages == that.maxUsages && Objects.equals(code, that.code) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, maxUsages, country);
    }
}
