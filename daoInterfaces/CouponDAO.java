package daoInterfaces;

import java.util.Collection;

import exceptions.CouponSystemException;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;

public interface CouponDAO {

	void createCoupon(Coupon coupon) throws CouponSystemException;

	void removeCoupon(Coupon coupon) throws CouponSystemException;

	void updateCoupon(Coupon coupon) throws CouponSystemException;

	Coupon getCoupon(long id) throws CouponSystemException;

	Collection<Coupon> getAllCoupons() throws CouponSystemException;

	Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException;

}
