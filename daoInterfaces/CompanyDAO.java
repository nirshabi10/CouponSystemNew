package daoInterfaces;

import java.util.Collection;

import exceptions.CouponSystemException;
import javaBeansClasses.Company;
import javaBeansClasses.Coupon;

public interface CompanyDAO {

	void createCompany(Company company) throws CouponSystemException;

	void removeCompany(Company company) throws CouponSystemException;

	void updateCompany(Company company) throws CouponSystemException;

	Company getCompany(long id) throws CouponSystemException;

	Collection<Company> getAllCompanies() throws CouponSystemException;

	Collection<Coupon> getCompanyCoupons(Company company) throws CouponSystemException;

	boolean login(String compName, String password) throws CouponSystemException;
}
