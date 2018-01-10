package facades;

import java.util.Collection;

import daoClasses.CompanyDBDAO;
import daoClasses.CouponDBDAO;
import daoClasses.CustomerDBDAO;
import exceptions.CouponSystemException;
import exceptions.LoginException;
import javaBeansClasses.Company;
import javaBeansClasses.Coupon;
import javaBeansClasses.Customer;

public class AdminFacade implements CouponClientFacade {

	private CustomerDBDAO customerDB = new CustomerDBDAO();
	private CompanyDBDAO companyDB = new CompanyDBDAO();
	private CouponDBDAO couponDB = new CouponDBDAO();

	public AdminFacade() {

	}

	/**
	 * The function creates a company to the company DB.
	 * 
	 * @param company
	 *            a Company type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a
	 *             company.
	 */

	public void createCompany(Company company) throws CouponSystemException {
		companyDB.createCompany(company);
	}

	/**
	 * The function removes a company from the company DB. It also removes the
	 * Company coupons from all the DB tables, including coupons that were
	 * already bought by customers.
	 * 
	 * @param company
	 *            a Company type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             company.
	 */

	public void removeCompany(Company company) throws CouponSystemException {

		Collection<Coupon> coupons = companyDB.getCompanyCoupons(company);
		for (Coupon coupon : coupons) {
			couponDB.removeCouponFromCustomerCoupon(coupon);
		}
		companyDB.removeCouponsOfCompanyFromCoupon(company);
		companyDB.removeCouponsOfCompanyFromJoin(company);
		companyDB.removeCompany(company);

	}

	/**
	 * The function updates specific details of a company in the company DB.
	 * Only password or Email can be updated.
	 * 
	 * @param company
	 *            a Company type Object.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to update the
	 *             company details.
	 */

	public void updateCompany(Company company) throws CouponSystemException {
		companyDB.updateCompany(company);

	}

	/**
	 * The function reads specific details of a company from the company DB,
	 * given a specific company's ID.
	 * 
	 * @param id
	 *            specifying the company's id.
	 * 
	 * @return a new Company object.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to update the
	 *             company details.
	 */

	public Company getCompany(long id) throws CouponSystemException {
		return companyDB.getCompany(id);
	}

	/**
	 * The function reads all the existing companies from the company DB.
	 * 
	 * @return a new Collection<Company> holding all the companies.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             companies details.
	 */
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		return companyDB.getAllCompanies();
	}

	/**
	 * The function creates a customer to the customer DB.
	 * 
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a
	 *             customer.
	 */
	public void createCustomer(Customer customer) throws CouponSystemException {
		customerDB.createCustomer(customer);
	}

	/**
	 * The function removes a customer from the customer DB. It also removes all
	 * customer's coupons from the joined customer-coupon DB.
	 * 
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             customer.
	 */

	public void removeCustomer(Customer customer) throws CouponSystemException {
		customerDB.removeCustomer(customer);
		customerDB.removeCustomerCouponsFromJoin(customer);

	}

	/**
	 * The function updates a customer's password on the customer DB.
	 * 
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public void updateCustomer(Customer customer) throws CouponSystemException {
		customerDB.updateCustomer(customer);

	}

	/**
	 * The function reads a customer's details from the customer DB.
	 * 
	 * @param id
	 *            a unique id of a customer.
	 * @return a Customer.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public Customer getCustomer(long id) throws CouponSystemException {
		return customerDB.getCustomer(id);
	}

	/**
	 * The function reads all the customers details from the customer DB.
	 * 
	 * @return a Collection of Customers.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		return customerDB.getAllCustomer();
	}

	/**
	 * The function's use is for login.
	 * 
	 * @param name
	 *            a String representing the user name.
	 * @param password
	 *            a String representing the password.
	 * @return True if the user name is "admin" and the password is "1234".
	 * 
	 * @throws LoginException
	 *             with a corresponding message when failing to login.
	 */

	public CouponClientFacade login(String name, String password, ClientType type) throws LoginException {
		if (name.equals("admin") && password.equals("1234")) {
			return this;
		} else {
			throw new LoginException("The username doesn't exist in the system or the password is wrong");
		}

	}
	
	public boolean companyNameIsTaken(String compName) {
		boolean taken = false;
		try
		{
			return companyDB.companyNameIsTaken(compName);
		} catch (CouponSystemException e)
		{
		
		}
		return taken;
		
		
	}

}
