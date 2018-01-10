package daoClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;

import connectionPool.ConnectionPool;
import daoInterfaces.CouponDAO;
import exceptions.CouponSystemException;
import javaBeansClasses.Company;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;
import javaBeansClasses.Customer;

public class CouponDBDAO implements CouponDAO {

	public CouponDBDAO() {

	}

	/**
	 * The function creates a coupon to the coupon DB.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a coupon.
	 */

	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		boolean alreadyExist = couponNameIsTaken(coupon.getTitle());
		if (alreadyExist == false) {

			String sql = "insert into Coupon(TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) values (?, ?, ?, ?, ?, ?, ?, ?)";
			Connection con = ConnectionPool.getInstance().getConnection();

			try (PreparedStatement pstmt = con.prepareStatement(sql);) {
				pstmt.setString(1, coupon.getTitle());
				pstmt.setDate(2, coupon.getStartDate());
				pstmt.setDate(3, coupon.getEndDate());
				pstmt.setInt(4, coupon.getAmount());
				pstmt.setString(5, coupon.getType().toString());
				pstmt.setString(6, coupon.getMessage());
				pstmt.setDouble(7, coupon.getPrice());
				pstmt.setString(8, coupon.getImage());

				pstmt.executeUpdate();

			} catch (SQLException e) {
				throw new CouponSystemException("Creating new coupon in the system failed");
			} finally {
				ConnectionPool.getInstance().returnConnection(con);
			}
		} else {
			throw new CouponSystemException("There is already a coupon with this name!");
		}

	}

	/**
	 * The function removes a coupon from all the relevent DB tables.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a coupon.
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		Coupon coup = getCoupon(coupon.getTitle());
		String sql1 = "DELETE FROM Coupon WHERE ID = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql1);) {

			pstmt.setLong(1, coup.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Removing coupon from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

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
	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		Coupon coup = getCoupon(coupon.getTitle());
		String sql = "Update Coupon SET END_DATE = ?, PRICE = ? WHERE ID = ? ";

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setDate(1, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setDouble(2, coupon.getPrice());
			pstmt.setLong(3, coup.getId());

			pstmt.execute();

		} catch (SQLException e) {
			throw new CouponSystemException("Updating coupon details in the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

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
	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = " SELECT * FROM Coupon WHERE ID = ?";
		Coupon coupon = new Coupon();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading coupon with a specific ID from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupon;

	}

	/**
	 * The function reads a coupon details from the coupon DB.
	 * 
	 * @param title
	 *            a String stating the coupon's title.
	 * @return a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read a coupon.
	 */
	public Coupon getCoupon(String title) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = " SELECT * FROM Coupon WHERE TITLE = ?";
		Coupon coupon = new Coupon();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading coupon with a specific ID from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupon;

	}

	/**
	 * The function reads all coupons details from the coupon DB.
	 * 
	 * 
	 * @return a new Collection of coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             coupons.
	 */
	@Override
	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = " SELECT * FROM Coupon ";
		Collection<Coupon> coupons = new LinkedHashSet<>();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
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
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading all the coupons from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;
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
	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = " SELECT * FROM Coupon WHERE TYPE = ? ";
		Collection<Coupon> coupons = new LinkedHashSet<>();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, type.toString());
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
			throw new CouponSystemException("Reading all the coupons of specific type from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;
	}

	/**
	 * The function reads all coupons up to a specific price from the coupon DB.
	 * 
	 * @param double
	 *            a number indicating the price.
	 * @return a new Collection of coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             coupons.
	 */

	public Collection<Coupon> getCouponUpToPrice(double price) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> coupons = new LinkedHashSet<>();
		String sql = "SELECT * FROM Coupon WHERE PRICE <= " + price;
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
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
			throw new CouponSystemException("Reading all the coupons up to specific price from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;

	}

	/**
	 * The function reads all coupons up to a specific date from the coupon DB.
	 * 
	 * @param date
	 *            a string indicating the date.
	 * @return a new Collection of coupons.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the
	 *             coupons.
	 */

	public Collection<Coupon> getCouponUpToDate(String date) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> coupons = new LinkedHashSet<>();
		String sql = "SELECT * FROM coupon WHERE END_DATE <= ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setDate(1, java.sql.Date.valueOf(date));
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
			throw new CouponSystemException("Reading all the coupons up to specific date from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;

	}

	/**
	 * The function removes a coupon from the joined customer-coupon DB.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a coupon.
	 */
	public void removeCouponFromCustomerCoupon(Coupon coupon) throws CouponSystemException {
		String sql = "DELETE FROM customer_coupon WHERE COUPON_ID = ?";
		Coupon coup = getCoupon(coupon.getTitle());
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, coup.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Removing the connection between the coupon and the customers who purchased them from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function removes a coupon from the joined company-coupon DB.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a coupon.
	 */
	public void removeCouponFromCompanyCoupon(Coupon coupon) throws CouponSystemException {
		String sql = "DELETE FROM company_coupon WHERE COUPON_ID = ?";
		Coupon coup = getCoupon(coupon.getTitle());
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, coup.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Removing the connection between the coupon and the company who purchased them from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function creates a coupon to the joined Company-coupon DB.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @param company
	 *            a Company type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a coupon.
	 */

	public void connectCompanyToCoupon(Company company, Coupon coupon) throws CouponSystemException {

		String sql = "INSERT INTO Company_Coupon VALUES (?, ?)";
		Connection con = ConnectionPool.getInstance().getConnection();
		Coupon coup = getCoupon(coupon.getTitle());
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, company.getId());
			pstmt.setLong(2, coup.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Connecting coupon to a company failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function creates a coupon to the joined Customer-coupon DB.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @param customer
	 *            a Customer type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to create a coupon.
	 */

	public void connectCustomerToCoupon(Customer customer, Coupon coupon) throws CouponSystemException {

		String sql = "INSERT INTO Customer_Coupon VALUES (?, ?)";
		Connection con = ConnectionPool.getInstance().getConnection();
		Coupon coup = getCoupon(coupon.getTitle());
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, customer.getId());
			pstmt.setLong(2, coup.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Connecting coupon to a customer who purchased it failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function updates the amount of a specific coupon after a purchase by
	 * a customer was made. The amount is reduced by 1.
	 * 
	 * @param coupon
	 *            a Coupon type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to update the
	 *             coupon's amount.
	 */

	public void updateCouponAmount(Coupon coupon) throws CouponSystemException {

		String sql = "UPDATE Coupon SET AMOUNT = ? WHERE ID = ?";
		Coupon coup = getCoupon(coupon.getTitle());
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, coupon.getAmount() - 1);
			pstmt.setLong(2, coup.getId());
			pstmt.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("Updating coupon amount failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function checks if the coupon's title is already taken.
	 * 
	 * @param title
	 *            a String representing the coupon's title.
	 * @return True if the name is already taken.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to execute the
	 *             function.
	 */

	public boolean couponNameIsTaken(String title) throws CouponSystemException {
		boolean taken = false;
		String sql = "SELECT * FROM Coupon WHERE TITLE = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				taken = true;
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Checking if COUPON already exist in the DB failed");
		}
		return taken;
	}

}
