package com.interview.empik.coupons.model;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class UseCoupon {

    @NotBlank(message = "Coupon code cannot be blank")
    private String code;
    @NotBlank(message = "User cannot be blank")
    private String user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code != null) {
            this.code = code.toUpperCase();
        } else {
            this.code = null;
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UseCoupon useCoupon = (UseCoupon) o;
        return Objects.equals(code, useCoupon.code) && Objects.equals(user, useCoupon.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, user);
    }
}
