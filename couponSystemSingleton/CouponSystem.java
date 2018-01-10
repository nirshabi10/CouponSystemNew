package couponSystemSingleton;

import connectionPool.ConnectionPool;
import exceptions.CouponSystemException;
import facades.AdminFacade;
import facades.ClientType;
import facades.CompanyFacade;
import facades.CouponClientFacade;
import facades.CustomerFacade;
import runningThreads.DailyCouponExpirationTask;

public class CouponSystem {

	DailyCouponExpirationTask dailyTask;
	Thread currentThread;
	private static CouponSystem instance;

	private CouponSystem() {

		dailyTask = new DailyCouponExpirationTask();
		currentThread = new Thread(dailyTask);
		currentThread.start();
	}

	public static synchronized CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
			return instance;
		} else {
			return instance;
		}

	}

	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemException {
		if (clientType.equals(ClientType.valueOf("ADMIN"))) {
			try {
				return new AdminFacade().login(name, password, clientType);
			} catch (CouponSystemException e) {
			}
		} else if (clientType.equals(ClientType.valueOf("COMPANY"))) {
			try {
				return new CompanyFacade().login(name, password, clientType);
			} catch (CouponSystemException e) {
			}
		} else if (clientType.equals(ClientType.valueOf("CUSTOMER"))) {
			try {
				return new CustomerFacade().login(name, password, clientType);
			} catch (CouponSystemException e) {
			}
		}
		throw new CouponSystemException("The user name doesn't exist in the system or the password is wrong");

	}

	public void shutdown() {
		dailyTask.stoptask();
		currentThread.interrupt();
		ConnectionPool.getInstance().closeAllConnections();
	}
}
