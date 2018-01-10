package tests;

import java.util.Collection;

import couponSystemSingleton.CouponSystem;
import exceptions.CouponSystemException;
import facades.AdminFacade;
import facades.ClientType;
import javaBeansClasses.Company;

public class TestAdmin {

	public static void main(String[] args) {

		ClientType type = ClientType.ADMIN;
		// Company company = new Company();
		// company.setCompName("Sector");
		// company.setPassword("3333");
		// company.setEmail("Sector@gmail.com");
		//
		// Customer customer = new Customer();
		// customer.setCustName("ran");
		// customer.setPassword("2121");

		AdminFacade a;
		try {
			a = (AdminFacade) CouponSystem.getInstance().login("admin", "1234", type);
			Collection<Company> companies = a.getAllCompanies();
			System.out.println(companies);
			// a.createCompany(company);
			// company.setPassword("1010");
			// company.setEmail("SectorSupport@gmail.com");
			// a.updateCompany(company);
			// System.out.println(a.getCompany(1));
			// a.createCustomer(customer);
			// customer.setPassword("9988");
			// a.updateCustomer(customer);
			// System.out.println(a.getCustomer(2));
			// a.removeCompany(company);
			// a.removeCustomer(customer);
			// System.out.println(a.getAllCompanies());
			// System.out.println(a.getAllCustomers());
			// CouponSystem.getInstance().shutdown();

		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
