package daoClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import connectionPool.ConnectionPool;
import daoInterfaces.CompanyDAO;
import exceptions.CouponSystemException;
import javaBeansClasses.Company;
import javaBeansClasses.Coupon;
import javaBeansClasses.CouponType;


public class CompanyDBDAO implements CompanyDAO {

	public CompanyDBDAO() {

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

	@Override
	public void createCompany(Company company) throws CouponSystemException {
		boolean Taken = companyNameIsTaken(company.getCompName());
		if (Taken == false) {

			Connection con = ConnectionPool.getInstance().getConnection();
			String sql = "INSERT into Company (COMP_NAME,PASSWORD,EMAIL) values(?,?,?)";
			try (PreparedStatement pstmt = con.prepareStatement(sql);) {

				pstmt.setString(1, company.getCompName());
				pstmt.setString(2, company.getPassword());
				pstmt.setString(3, company.getEmail());
				pstmt.executeUpdate();

			} catch (SQLException e) {
				throw new CouponSystemException("Creating new company in the system failed");
			} finally {
				ConnectionPool.getInstance().returnConnection(con);
			}
		} else {
			throw new CouponSystemException("There is already a company with this name!");
		}

	}

	/**
	 * The function removes a company from the company DB.
	 * 
	 * @param company
	 *            a Company type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             company.
	 */

	@Override
	public void removeCompany(Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "DELETE FROM Company WHERE COMP_NAME = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, company.getCompName());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Removing company from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

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
	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "UPDATE Company SET PASSWORD = ?, EMAIL = ? WHERE COMP_NAME = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, company.getPassword());
			pstmt.setString(2, company.getEmail());
			pstmt.setString(3, company.getCompName());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("Updating comapny details in the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

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

	@Override
	public Company getCompany(long id) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM Company WHERE ID = ?";
		Company company = new Company();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));

			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading company with a specific ID from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

		return company;
	}

	/**
	 * The function reads specific details of a company from the company DB,
	 * given a specific company's name.
	 * 
	 * @param compName
	 *            a String specifying the company's name.
	 * @return a new Company object.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to read the company
	 *             details.
	 */
	public Company getCompanyByName(String compName) throws CouponSystemException {

		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM Company WHERE COMP_NAME = ?";
		Company company = new Company();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, compName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));

			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading company with a specific name from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return company;
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
	@Override
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM Company";
		Collection<Company> companies = new LinkedHashSet<>();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Company company = new Company();
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
				companies.add(company);
			}
		}

		catch (SQLException e) {
			throw new CouponSystemException("Reading all the companies from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return companies;
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

	@Override
	public Collection<Coupon> getCompanyCoupons(Company company) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();
		Company comp = getCompanyByName(company.getCompName());
		String sql = "SELECT * FROM Coupon inner join Company_coupon ON Coupon.ID = Company_coupon.COUPON_ID WHERE COMP_ID = ?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setLong(1, comp.getId());
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
			throw new CouponSystemException("Reading the company coupons from the system failed");
		}finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;
	}

	/**
	 * The function's use is for login in.
	 * 
	 * @param compName
	 *            a company's name.
	 * @param password
	 *            a company's password.
	 * @return True if the company exists and the password is correct.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to login.
	 */

	@Override
	public boolean login(String compName, String password) throws CouponSystemException {
		String sql = "SELECT COMP_NAME, PASSWORD FROM Company WHERE COMP_NAME = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		boolean loginCheck = false;
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, compName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (compName.equals(rs.getString(1)) && password.equals(rs.getString(2))) {
					loginCheck = true;
				}
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Company login to the system failed");

		}finally{
			ConnectionPool.getInstance().returnConnection(con);
		}

		return loginCheck;
	}

	/**
	 * The function removes coupons of a specific company from the joined
	 * company_coupons DB.
	 * 
	 * @param company
	 *            a Company type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             company.
	 */

	public void removeCouponsOfCompanyFromJoin(Company company) throws CouponSystemException {
		String sql = "DELETE FROM Company_coupon WHERE COMP_ID = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		Company comp = getCompanyByName(company.getCompName());
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, comp.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Removing the connection between coupons and the company from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function removes coupons of a specific company from the Coupon's DB.
	 * 
	 * @param company
	 *            a Company type Object.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             company.
	 */

	public void removeCouponsOfCompanyFromCoupon(Company company) throws CouponSystemException {
		String sql = "DELETE FROM Coupon WHERE ID = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> coupons = new HashSet<>();
		coupons = getCompanyCoupons(company);
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			for (Coupon coupon : coupons) {
				pstmt.setLong(1, coupon.getId());
				pstmt.executeUpdate();

			}

		} catch (SQLException e) {
			throw new CouponSystemException("Removing coupons of the company from the system failed");
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	/**
	 * The function checks if the name of the company already exists in the DB.
	 * 
	 * @param compName
	 *            a String representing the company's name.
	 * @return true if the name already exists. False if it doesn't.
	 * @throws CouponSystemException
	 *             with a corresponding message when failing to remove a
	 *             company.
	 */

	public boolean companyNameIsTaken(String compName) throws CouponSystemException {
		boolean taken = false;
		String sql = "SELECT * FROM Company WHERE COMP_NAME = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, compName);
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
