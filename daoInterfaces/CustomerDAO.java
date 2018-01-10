package daoInterfaces;

import java.util.Collection;

import exceptions.CouponSystemException;
import javaBeansClasses.Coupon;
import javaBeansClasses.Customer;

public interface CustomerDAO {

	void createCustomer(Customer customer) throws CouponSystemException;

	void removeCustomer(Customer customer) throws CouponSystemException;

	void updateCustomer(Customer customer) throws CouponSystemException;

	Customer getCustomer(long id) throws CouponSystemException;

	Collection<Customer> getAllCustomer() throws CouponSystemException;

	//Collection<Coupon> getCoupons() throws CouponSystemException;
	
	Collection<Coupon> getCustomerCoupons(Customer customer) throws CouponSystemException;

	boolean login(String custName, String password) throws CouponSystemException;
}
