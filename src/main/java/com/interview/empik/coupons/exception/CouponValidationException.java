package com.interview.empik.coupons.exception;

public class CouponValidationException extends Exception {

    private final int errorCode;
    public CouponValidationException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
