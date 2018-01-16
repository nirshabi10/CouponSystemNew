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
		switch(clientType) {
			case ADMIN:
				return new AdminFacade().login(name, password, clientType);
			case COMPANY:
				return new CompanyFacade().login(name, password, clientType);
			case CUSTOMER:
				return new CustomerFacade().login(name, password, clientType);			
		}
		return null;
	}

	public void shutdown() {
		dailyTask.stoptask();
		currentThread.interrupt();
		ConnectionPool.getInstance().closeAllConnections();
	}
}
