package com.interview.empik.coupons.model;

import java.util.Objects;

public class CountryByIp {

    private String status;
    private String country;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        CountryByIp that = (CountryByIp) o;
        return Objects.equals(status, that.status) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, country);
    }
}
