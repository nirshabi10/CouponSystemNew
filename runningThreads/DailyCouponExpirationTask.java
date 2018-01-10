package runningThreads;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import daoClasses.CouponDBDAO;
import exceptions.CouponSystemException;
import javaBeansClasses.Coupon;

public class DailyCouponExpirationTask implements Runnable {

	private CouponDBDAO couponDB = new CouponDBDAO();
	private boolean quit = false;

	public DailyCouponExpirationTask() {

	}

	/**
	 * This is a daily thread. The thread run always once the program has been
	 * started. The thread creates a new date, then comparing the date to the
	 * <code> End Date </code> of the coupons and removes any expired coupons.
	 * The thread then goes to sleep for a day.
	 * 
	 */

	@Override
	public void run() {
		while (quit == false) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Collection<Coupon> coupons = couponDB.getCouponUpToDate(sdf.format(date));
				for (Coupon coupon : coupons) {
					couponDB.removeCouponFromCompanyCoupon(coupon);
					couponDB.removeCouponFromCustomerCoupon(coupon);
					couponDB.removeCoupon(coupon);
				}

			} catch (CouponSystemException e1) {
			}

			try {
				Thread.sleep(86400000);
			} catch (InterruptedException e) {

			}
		}

	}

	/**
	 * This is a quit function. it interferes with the daily task and stops it.
	 * 
	 */

	public void stoptask() {
		this.quit = true;
	}

}
