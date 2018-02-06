//problem with the dates in create/update

package facades;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import daoClasses.CompanyDBDAO;
import daoClasses.CouponDBDAO;
import exceptions.CouponSystemException;
import exceptions.LoginException;
import javaBeansClasses.Company;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;

public class CompanyFacade implements CouponClientFacade {

	private Company thisCompany;
	private CouponDBDAO couponDB = new CouponDBDAO();
	private CompanyDBDAO companyDB = new CompanyDBDAO();

	public CompanyFacade() {

	}

	/**
	 * The function creates a coupon to the coupon DB. It also creates a coupon
	 * in the joined company-coupon DB.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a coupon.
	 */

	public void createCoupon(Coupon coupon) throws CouponSystemException {
		couponDB.createCoupon(coupon);
		couponDB.connectCompanyToCoupon(this.thisCompany, coupon);
	}

	/**
	 * The function removes a coupon from the coupon DB. It also removes the
	 * coupon from the two joined DB tables.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a coupon.
	 */

	public void removeCoupon(Coupon coupon) throws CouponSystemException {

		couponDB.removeCouponFromCustomerCoupon(coupon);
		couponDB.removeCouponFromCompanyCoupon(coupon);
		couponDB.removeCoupon(coupon);
	}

	/**
	 * The function updates a coupon details in the coupon DB. Only the End-Date
	 * and the price can be updated.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to update a coupon.
	 */

	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		couponDB.updateCoupon(coupon);
	}

	/**
	 * The function reads a coupon details from the coupon DB.
	 * 
	 * @param id
	 *            a coupon's id.
	 * @return a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read a coupon.
	 */

	public Coupon getCoupon(long id) throws CouponSystemException {
		return couponDB.getCoupon(id);
	}

	/**
	 * This function is used to get all the compani's details after login in.
	 * 
	 * @return a new Company object.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to update the
	 *             company details.
	 */

	public Company getCompany() throws CouponSystemException {
		return companyDB.getCompany(this.thisCompany.getId());
	}

	/**
	 * The function reads all the existing coupons of a specific company from
	 * the joined company-coupon DB. The function first holds in a collection
	 * all of the company coupons from the joined table, then going over all of
	 * the existing coupons in the coupon DB, searching for the id's of the
	 * company's coupons.
	 * 
	 * @param company
	 *            a Company type Object.
	 * @return a new Collection of coupons holding all the coupons of a specific
	 *         company.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the coupon
	 *             details.
	 */

	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		return companyDB.getCompanyCoupons(this.thisCompany);
	}

	/**
	 * The function reads all coupons of a specific type from the coupon DB.
	 * 
	 * @param type
	 *            a CouponType.
	 * @return a new Collection of coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             coupons.
	 */

	public Collection<Coupon> getCouponsByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		coupons = companyDB.getCompanyCoupons(this.thisCompany);
		Collection<Coupon> coupons2 = new HashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getType().equals(type)) {
				coupons2.add(coupon);
			}
		}
		return coupons2;

	}

	/**
	 * The function reads all coupons up to a specific price from the coupon DB.
	 * 
	 * @param price
	 *            a <code>double </code> representing the price amount.
	 * @return a new Collection of coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             coupons.
	 */

	public Collection<Coupon> getCouponsByPrice(double price) throws CouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		coupons = companyDB.getCompanyCoupons(this.thisCompany);
		Collection<Coupon> coupons2 = new HashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice()<=price) {
				coupons2.add(coupon);
			}
		}
		return coupons2;

	}

	/**
	 * The function reads all coupons up to a specific date from the coupon DB.
	 * 
	 * @param date
	 *            a <code>Date </code> representing the given date.
	 * @return a new Collection of coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             coupons.
	 */

	public Collection<Coupon> getCouponsByDate(Date date) throws CouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		coupons = companyDB.getCompanyCoupons(this.thisCompany);
		Collection<Coupon> coupons2 = new HashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getEndDate().before(date)) {
				coupons2.add(coupon);
			}
		}
		return coupons2;

	}

	/**
	 * The function's use is for login.
	 * 
	 * @param name
	 *            a String representing the user name.
	 * @param password
	 *            a String representing the password.
	 * @return True if the company exists and the password is correct.
	 * 
	 * @throws LoginException
	 *             with a corresponding message when failing to login.
	 */

	@Override
	public CouponClientFacade login(String name, String password, ClientType type) throws CouponSystemException {
		if (companyDB.login(name, password)) {
			this.thisCompany = companyDB.getCompanyByName(name);
			return this;
		} else {
			throw new LoginException(
					"The username of the company doesn't exist in the system or the password is wrong");
		}
	}

}