package com.interview.empik.coupons.model;

import java.util.Objects;

public class SuccessResponse {

    private final int code;
    private final String message;

    public SuccessResponse(String message) {
        this.code = 2000;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SuccessResponse that = (SuccessResponse) o;
        return code == that.code && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }
}
