package dbTables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDB {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB1", "root", "1234");
			Statement stmt = con.createStatement();
			stmt.executeUpdate(createCompany);
			stmt.executeUpdate(createCustomer);
			stmt.executeUpdate(createCoupon);
			stmt.executeUpdate(createCustomerCoupon);
			stmt.executeUpdate(createCompanyCoupon);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String createCompany = "CREATE table Company (" + "ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
			+ "COMP_NAME VARCHAR(40)," + "PASSWORD VARCHAR(40)," + "EMAIL VARCHAR(70))";

	private static String createCustomer = "CREATE table Customer (" + "ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
			+ "CUST_NAME VARCHAR(40)," + "PASSWORD VARCHAR(40))";

	private static String createCoupon = "CREATE table Coupon (" + "ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
			+ "TITLE VARCHAR(70)," + "START_DATE DATE," + "END_DATE DATE," + "AMOUNT INTEGER," + "TYPE VARCHAR(50),"
			+ "MESSAGE VARCHAR(255)," + "PRICE DOUBLE," + "IMAGE VARCHAR(255))";

	private static String createCustomerCoupon = "CREATE table Customer_coupon (" + "CUST_ID BIGINT,"
			+ "COUPON_ID BIGINT," + "PRIMARY KEY (CUST_ID,COUPON_ID))";

	private static String createCompanyCoupon = "CREATE table Company_coupon (" + "COMP_ID BIGINT,"
			+ "COUPON_ID BIGINT," + "PRIMARY KEY (COMP_ID,COUPON_ID))";

	public static String addCoupon(String title, String start, String end, int amount, String type, String message,
			double price, String image) {
		return "INSERT INTO Coupon (TITLE,START_DATE,END_DATE,AMOUNT,TYPE,MESSAGE,PRICE,IMAGE) " + "VALUES ('" + title
				+ "','" + start + "','" + end + "'," + amount + ",'" + type + "','" + message + "'," + price + ",'"
				+ image + "')";

	}

	public static String addCustomer(String customerName, String password) {
		return "INSERT INTO Customer (CUST_NAME,PASSWORD) " + "VALUES ('" + customerName + "','" + password + "')";

	}

	public static String addCompany(String companyName, String password, String email) {
		return "INSERT INTO Company (COMP_NAME,PASSWORD,EMAIL) " + "VALUES ('" + companyName + "','" + password + "','"
				+ email + "')";
	}

}
