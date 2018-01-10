package connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import exceptions.CouponSystemException;

public class ConnectionPool {

	private static ConnectionPool instance = new ConnectionPool();
	private Set<Connection> connections = new HashSet<>();
	private static final int MAX_CONNECTIONS = 10;
	private String url = "jdbc:mysql://localhost:3306/DB1";
	private boolean shutdownMode = false;

	private ConnectionPool() {
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = DriverManager.getConnection(url, "root", "1234");
				connections.add(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static ConnectionPool getInstance() {
		return instance;
	}

	/**
	 * This is a synchronized function. The purpose of this function is to
	 * manage a series of connections to the DB. The function checks if there
	 * are any available connections. If there is one, a connection is being
	 * returned. If none are available, the function will wait until one will be
	 * available.
	 * 
	 * @return a <code>Connection</code> if it's possible. Or <code>NULL</code>
	 *         if we are now shutting down the system
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when to execute the function.
	 */
	public synchronized Connection getConnection() {
		if (shutdownMode == false) {
			while (connections.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Iterator<Connection> it = connections.iterator();
			Connection con = it.next();
			it.remove();
			return con;
		} else {
			try {
				throw new CouponSystemException("the system is in shutdown process, cant get a connection");
			} catch (CouponSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * This is a synchronized function. The purpose of this function is to
	 * manage a series of connections to the DB. The function returns a
	 * <code>connection</code> to the connection pool.
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when to execute the function.
	 */

	public synchronized void returnConnection(Connection con) {
		connections.add(con);
		notify();
	}

	/**
	 * This is a synchronized function. The purpose of this function is to shut
	 * down the system. The function will stop the use of function
	 * {@link #getConnection}, will then wait 5 seconds for all other
	 * connections to return, and will then close all connections.
	 * 
	 * 
	 * @throws CouponSystemException
	 *             with a corresponding message when to execute the function.
	 */

	public synchronized void closeAllConnections() {
		try {
			shutdownMode = true;
			wait(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Connection con : connections) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		connections.removeAll(connections);
	}
}
