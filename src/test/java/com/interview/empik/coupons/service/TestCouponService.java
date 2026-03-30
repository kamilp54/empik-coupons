package com.interview.empik.coupons.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.interview.empik.coupons.exception.CouponValidationException;
import com.interview.empik.coupons.model.Coupon;
import com.interview.empik.coupons.model.CreateCoupon;
import com.interview.empik.coupons.model.UseCoupon;
import com.interview.empik.coupons.repository.CouponRepository;
import com.interview.empik.coupons.repository.UserCouponUsageRepository;
import com.interview.empik.coupons.validator.CouponValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class TestCouponService {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserCouponUsageRepository userCouponUsageRepository;

    private CouponService couponService;

    @BeforeEach
    void setup() {
        CouponValidator couponValidator = new CouponValidator(userCouponUsageRepository, couponRepository);
        UserCouponUsageService userCouponUsageService = new UserCouponUsageService(userCouponUsageRepository);
        couponService = new CouponService(couponRepository, userCouponUsageService, couponValidator);
    }

    @Test
    void shouldSuccessfullyCreateCoupon() throws CouponValidationException {
        //given
        CreateCoupon coupon = getCreateCoupon();
        doReturn(false).when(couponRepository).checkIfCouponExists("TEST");
        //when
        couponService.createCoupon(coupon);
        //then
        verify(couponRepository).createCoupon(coupon);
    }

    @Test
    void shouldThrowExceptionForDuplicatedCode() {
        //given
        CreateCoupon coupon = getCreateCoupon();
        doReturn(true).when(couponRepository).checkIfCouponExists("TEST");
        //when
        CouponValidationException exception = assertThrows(CouponValidationException.class, () -> couponService.createCoupon(coupon));
        //then
        verifyNoMoreInteractions(couponRepository);
        assertThat(exception.getErrorCode()).isEqualTo(4005);
        assertThat(exception.getMessage()).isEqualTo("The coupon TEST already exists");
    }

    @Test
    void shouldSuccessfullyUseCoupon() throws CouponValidationException {
        //given
        UseCoupon useCoupon = getUseCoupon();
        Coupon coupon = getCoupon();
        doReturn(coupon).when(couponRepository).findCouponByCode(anyString());
        doReturn(false).when(userCouponUsageRepository).checkIfUserUsedCoupon(anyLong(), anyString());
        //when
        couponService.useCoupon(useCoupon, "Poland");
        //then
        verify(couponRepository).updateUsage(anyLong());
        verify(userCouponUsageRepository).addUserCouponUsage(anyLong(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenCouponNotExists() {
        //given
        UseCoupon useCoupon = getUseCoupon();
        doReturn(null).when(couponRepository).findCouponByCode(anyString());
        //when
        CouponValidationException exception = assertThrows(CouponValidationException.class, () -> couponService.useCoupon(useCoupon, "Poland"));
        //then
        verifyNoMoreInteractions(couponRepository, userCouponUsageRepository);
        assertThat(exception.getErrorCode()).isEqualTo(4001);
        assertThat(exception.getMessage()).isEqualTo("The coupon does not exist");
    }

    @Test
    void shouldThrowExceptionWhenCountryMismatch() {
        //given
        UseCoupon useCoupon = getUseCoupon();
        Coupon coupon = getCoupon();
        doReturn(coupon).when(couponRepository).findCouponByCode(anyString());
        //when
        CouponValidationException exception = assertThrows(CouponValidationException.class, () -> couponService.useCoupon(useCoupon, "USA"));
        //then
        verifyNoMoreInteractions(couponRepository, userCouponUsageRepository);
        assertThat(exception.getErrorCode()).isEqualTo(4002);
        assertThat(exception.getMessage()).isEqualTo("The coupon TEST can only be used in Poland");
    }

    @Test
    void shouldThrowExceptionWhenUserUsedCoupon() {
        //given
        UseCoupon useCoupon = getUseCoupon();
        Coupon coupon = getCoupon();
        doReturn(coupon).when(couponRepository).findCouponByCode(anyString());
        doReturn(true).when(userCouponUsageRepository).checkIfUserUsedCoupon(anyLong(), anyString());
        //when
        CouponValidationException exception = assertThrows(CouponValidationException.class, () -> couponService.useCoupon(useCoupon, "Poland"));
        //then
        verifyNoMoreInteractions(couponRepository, userCouponUsageRepository);
        assertThat(exception.getErrorCode()).isEqualTo(4003);
        assertThat(exception.getMessage()).isEqualTo("The user has already used this coupon");
    }

    @Test
    void shouldThrowExceptionWhenMaxUsageExceeded() {
        //given
        UseCoupon useCoupon = getUseCoupon();
        Coupon coupon = getCoupon();
        coupon.setCurrentUsages(1);
        doReturn(coupon).when(couponRepository).findCouponByCode(anyString());
        doReturn(false).when(userCouponUsageRepository).checkIfUserUsedCoupon(anyLong(), anyString());
        //when
        CouponValidationException exception = assertThrows(CouponValidationException.class, () -> couponService.useCoupon(useCoupon, "Poland"));
        //then
        verifyNoMoreInteractions(couponRepository, userCouponUsageRepository);
        assertThat(exception.getErrorCode()).isEqualTo(4004);
        assertThat(exception.getMessage()).isEqualTo("The maximum number of coupon uses has been reached");

    }

    private CreateCoupon getCreateCoupon() {
        CreateCoupon coupon = new CreateCoupon();
        coupon.setCode("TEST");
        coupon.setCountry("Poland");
        coupon.setMaxUsages(1);
        return coupon;
    }

    private UseCoupon getUseCoupon() {
        UseCoupon coupon = new UseCoupon();
        coupon.setCode("TEST");
        coupon.setUser("USER");
        return coupon;
    }

    private Coupon getCoupon() {
        Coupon coupon = new Coupon();
        coupon.setCouponId(1L);
        coupon.setCode("TEST");
        coupon.setCountry("Poland");
        coupon.setCurrentUsages(0);
        coupon.setMaxUsages(1);
        coupon.setCreatedDate(Timestamp.from(Instant.now()));
        return coupon;
    }
}
