package daoClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import connectionPool.ConnectionPool;
import daoInterfaces.CustomerDAO;
import exceptions.CouponSystemException;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;
import javaBeansClasses.Customer;

public class CustomerDBDAO implements CustomerDAO {

	public CustomerDBDAO() {

	}

	/**
	 * The function creates a customer to the customer DB. The function first
	 * checks if the customer already exists by comparing the name of the given
	 * customer to the customers in the DB.
	 * 
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a
	 *             customer.
	 */
	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {
		boolean alreadyExist = customerNameIsTaken(customer.getCustName());
		if (alreadyExist == false) {

			Connection con = ConnectionPool.getInstance().getConnection();
			String sql = "INSERT INTO Customer (CUST_NAME,PASSWORD) VALUES(?,?)";
			try (PreparedStatement pstmt = con.prepareStatement(sql);) {

				pstmt.setString(1, customer.getCustName());
				pstmt.setString(2, customer.getPassword());
				pstmt.executeUpdate();

			} catch (SQLException e) {
				throw new CouponSystemException("Creating new customer in the system failed");
			} finally {
				ConnectionPool.getInstance().returnConnection(con);
			}
		} else {
			throw new CouponSystemException("There is already a customer with this name!");

		}

	}

	/**
	 * The function removes a customer from the customer DB.
	 * 
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             customer.
	 */
	@Override
	public void removeCustomer(Customer customer) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		Customer cust = readCustomerByName(customer.getCustName());
		String sql = "DELETE FROM Customer WHERE ID = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, cust.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Removing customer from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

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

	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		Customer cust = readCustomerByName(customer.getCustName());
		String sql = "UPDATE Customer SET PASSWORD = ? WHERE ID = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, customer.getPassword());
			pstmt.setLong(2, cust.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Updating customer details in the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

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

	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM Customer WHERE ID = ?";
		Customer customer = new Customer();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Reading customer with a specific ID from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		if (customer.getId() == 0) {
			System.out.println("There is not a customer with this ID, returning an empty represntation of customer");
		}
		return customer;

	}

	/**
	 * The function reads a customer's details from the customer DB.
	 * 
	 * @param customerName
	 *            a String representing the name of a customer.
	 * @return a Customer. If no customer was found under the specific name, an
	 *         empty customer Object will be returned.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public Customer readCustomerByName(String customerName) throws CouponSystemException {

		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM Customer WHERE CUST_NAME = ?";
		Customer customer = new Customer();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, customerName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));

			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading customer with a specific name from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		if (customer.getId() == 0) {
			System.out.println("There is not a customer with this ID, returning an empty represntation of customer");
		}
		return customer;
	}

	/**
	 * The function reads all the customers details from the customer DB.
	 * 
	 * @return a Collection of Customers.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	@Override
	public Collection<Customer> getAllCustomer() throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM Customer";
		Collection<Customer> customers = new LinkedHashSet<>();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Customer customer = new Customer();
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
				customers.add(customer);
			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading all the customers from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return customers;
	}

	/**
	 * The function gets a collection of coupons from the joined
	 * customer-coupons DB. The function then uses the id's collected from the
	 * joined DB table, and uses them in order to get the all the specific
	 * coupons from the coupon's DB.
	 * 
	 * 
	 * @param customer
	 *            a specific customer.
	 * @return a Collection of Coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	@Override
	public Collection<Coupon> getCustomerCoupons(Customer customer) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();
		String sql = "SELECT * FROM Coupon INNER JOIN Customer_coupon ON Coupon.ID = Customer_coupon.COUPON_ID WHERE CUST_ID = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setLong(1, customer.getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupons.add(coupon);
			}
		} catch (SQLException e) {

			throw new CouponSystemException("Reading the customer coupons from the system failed");
		}finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;
	}

	/**
	 * The function's use is for login.
	 * 
	 * @param custName
	 *            a company's name.
	 * @param password
	 *            a company's password.
	 * @return True if the customer exists and the password is correct.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to login.
	 */

	@Override
	public boolean login(String custName, String password) throws CouponSystemException {
		String sql = "SELECT CUST_NAME, PASSWORD FROM Customer WHERE CUST_NAME = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		boolean loginCheck = false;
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, custName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (custName.equals(rs.getString(1)) && password.equals(rs.getString(2))) {
					loginCheck = true;
				}
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Customer login to the system failed");

		}finally{
			ConnectionPool.getInstance().returnConnection(con);
		}

		return loginCheck;
	}

	/**
	 * The function removes a customer coupons from the joined customer-coupon
	 * DB.
	 * 
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a coupon.
	 */

	public void removeCustomerCouponsFromJoin(Customer customer) throws CouponSystemException {
		String sql = "DELETE FROM Customer_coupon WHERE CUST_ID = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, customer.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Removing the connection between coupons and the customer who purchased them from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function checks if the customer's name is already taken.
	 * 
	 * @param custName
	 *            a String representing the customer's name.
	 * @return True if the name is already taken.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public boolean customerNameIsTaken(String custName) throws CouponSystemException {
		boolean taken = false;
		String sql = "SELECT * FROM Customer WHERE CUST_NAME = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, custName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				taken = true;
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Checking if company already exist in the DB failed");
		}finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return taken;
	}

}
