package com.interview.empik.coupons.model;

import java.util.Objects;

public class ExceptionResponse {

    private final int code;
    private final Object errors;

    public ExceptionResponse(int code, Object errors) {
        this.code = code;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public Object getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionResponse that = (ExceptionResponse) o;
        return code == that.code && Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, errors);
    }
}
