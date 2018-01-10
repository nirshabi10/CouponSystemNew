package tests;

import java.util.Calendar;
import java.util.Date;

import couponSystemSingleton.CouponSystem;
import exceptions.CouponSystemException;
import facades.ClientType;
import facades.CustomerFacade;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;

public class TestCustomer {
	public static void main(String[] args) {
		Coupon coupon = new Coupon();
		coupon.setTitle("Casio luxury");
		coupon.setType(CouponType.WATCHES);
		coupon.setMessage("discount on Casio luxury watches");
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 6, 2);
		Date start = cal.getTime();
		cal.set(2018, 1, 10);
		Date end = cal.getTime();
		coupon.setAmount(10);
		coupon.setPrice(100);

		coupon.setStartDate(new java.sql.Date(start.getTime()));
		coupon.setEndDate(new java.sql.Date(end.getTime()));

		ClientType type = ClientType.CUSTOMER;
		CustomerFacade cust;
		try {
			cust = (CustomerFacade) CouponSystem.getInstance().login("Yossi", "1414", type);
			cust.purchaseCoupon(coupon);

			System.out.println(cust.getAllPurchasedCoupons());
			System.out.println(cust.getAllPurchasedCouponsByPrice(100));
			System.out.println(cust.getAllPurchasedCouponsByType(CouponType.WATCHES));
			System.out.println(cust.getAllPurchasedCouponsByPrice(90));
			System.out.println(cust.getAllPurchasedCouponsByType(CouponType.CAMPING));
			CouponSystem.getInstance().shutdown();
		} catch (CouponSystemException e) {

			e.printStackTrace();
		}

	}
}
