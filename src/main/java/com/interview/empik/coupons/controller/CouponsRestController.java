package com.interview.empik.coupons.controller;

import com.interview.empik.coupons.exception.CouponValidationException;
import com.interview.empik.coupons.model.CreateCoupon;
import com.interview.empik.coupons.model.SuccessResponse;
import com.interview.empik.coupons.model.UseCoupon;
import com.interview.empik.coupons.service.CountryService;
import com.interview.empik.coupons.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponsRestController {

    private final CouponService couponService;
    private final CountryService countryService;

    public CouponsRestController(CouponService couponService, CountryService countryService) {
        this.couponService = couponService;
        this.countryService = countryService;
    }

    @PutMapping("/use")
    public ResponseEntity<SuccessResponse> useCoupon(HttpServletRequest request, @Valid @RequestBody UseCoupon useCoupon) throws CouponValidationException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String country = countryService.getCountryByIP(ip);

        couponService.useCoupon(useCoupon, country);
        return ResponseEntity.ok(new SuccessResponse("The coupon was successfully used"));
    }

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createCoupon(@Valid @RequestBody CreateCoupon coupon) throws CouponValidationException {
        couponService.createCoupon(coupon);
        return ResponseEntity.ok(new SuccessResponse("The coupon was successfully created"));
    }

}
