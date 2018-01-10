package facades;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import daoClasses.CouponDBDAO;
import daoClasses.CustomerDBDAO;
import exceptions.CouponSystemException;
import exceptions.LoginException;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;
import javaBeansClasses.Customer;

public class CustomerFacade implements CouponClientFacade {

	private Customer thisCustomer;
	private CouponDBDAO couponDB = new CouponDBDAO();
	private CustomerDBDAO customerDB = new CustomerDBDAO();

	public CustomerFacade() {

	}
	public Customer getCustomer() throws CouponSystemException {
		return customerDB.getCustomer(this.thisCustomer.getId());
	}

	/**
	 * This function is used to purchase a coupon. A coupon get be purchased
	 * only if the coupon exists, the coupon's End Date hassn't passed and the
	 * amount of the coupon is bigger than 0. If a successful purchase has been
	 * made, the function adds the coupon to the joined customer-coupon DB, and
	 * updates the amount of the coupon.
	 * 
	 * @param coupon
	 *            A <code> Coupon </code> type parameter.
	 * 
	 */

	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
		Date date = new Date();
		Collection<Coupon> customerCoupons = customerDB.getCustomerCoupons(this.thisCustomer);
		boolean canPurchase = true;
		for (Coupon coup : customerCoupons) {
			if (coup.getTitle().equals(coupon.getTitle())) {
				canPurchase = false;
				throw new CouponSystemException("This customer already got this coupon");
			}
		}
		if (coupon.getAmount() == 0) {
			canPurchase = false;
			throw new CouponSystemException("This coupon is out of stock!");
		}
		if (coupon.getEndDate().before(date)) {
			canPurchase = false;
			throw new CouponSystemException("This coupon already expired! ");

		}

		if (canPurchase == true) {
			couponDB.connectCustomerToCoupon(this.thisCustomer, coupon);
			couponDB.updateCouponAmount(coupon);
		}
	}

	/**
	 * The function gets a collection of coupons representing the customer's
	 * coupons.
	 * 
	 * 
	 * @return a Collection of Coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public Collection<Coupon> getAllPurchasedCoupons() throws CouponSystemException {
		return customerDB.getCustomerCoupons(this.thisCustomer);
	}

	/**
	 * The function gets a collection of coupons from a specific type
	 * representing the customer's coupons.
	 * 
	 * @param type
	 *            A <code> CouponType </code> parameter.
	 * 
	 * 
	 * @return a Collection of Coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		coupons = customerDB.getCustomerCoupons(this.thisCustomer);
		Collection<Coupon> coupons2 = new HashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getType().equals(type)) {
				coupons2.add(coupon);
			}
		}
		return coupons2;
	}

	/**
	 * The function gets a collection of coupons ,who costs the same or less
	 * from a specific price, representing the customer's coupons.
	 * 
	 * @param price
	 *            A <code> double </code> parameter.
	 * 
	 * 
	 * @return a Collection of Coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) throws CouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		coupons = customerDB.getCustomerCoupons(this.thisCustomer);
		Collection<Coupon> coupons2 = new HashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice() <= price) {
				coupons2.add(coupon);
			}
		}
		return coupons2;
	}

	/**
	 * This function's use is to view all the coupons in the system.
	 * 
	 * @return a collection of Coupons.
	 * 
	 * @throws CouponSystemException
	 *             when failing to act.
	 */

	public Collection<Coupon> viewAllCoupons() throws CouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		coupons = couponDB.getAllCoupons();
		return coupons;
	}

	/**
	 * The function's use is for login.
	 * 
	 * @param name
	 *            a String representing the user name.
	 * @param password
	 *            a String representing the password.
	 * @return True if the customer exists and the password is correct.
	 * 
	 * @throws LoginException
	 *             with a corresponding message when failing to login.
	 */

	public CouponClientFacade login(String name, String password, ClientType type) throws CouponSystemException {
		if (customerDB.login(name, password)) {
			this.thisCustomer = customerDB.readCustomerByName(name);
			return this;
		} else {
			throw new LoginException(
					"The username of the customer doesn't exist in the system or the password is wrong");
		}
	}

}