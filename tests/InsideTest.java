
package tests;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;

import daoClasses.CouponDBDAO;
import exceptions.CouponSystemException;
import javaBeansClasses.Coupon;

public class InsideTest {

	public static void main(String[] args) {
		CouponDBDAO couponDB = new CouponDBDAO();
		Date date = new Date();
		System.out.println(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf);
		System.out.println(sdf.format(date));
		try {
			Collection<Coupon> coupons = couponDB.getCouponUpToDate(sdf.format(date));
			for (Coupon coupon : coupons) {
				couponDB.removeCouponFromCompanyCoupon(coupon);
				couponDB.removeCouponFromCustomerCoupon(coupon);
				couponDB.removeCoupon(coupon);
			}

		} catch (CouponSystemException e1) {
			System.out.println("something went wrong");
		}

	}
}
