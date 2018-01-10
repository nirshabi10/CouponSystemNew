package facades;

import exceptions.CouponSystemException;
import exceptions.LoginException;

public interface CouponClientFacade {

	CouponClientFacade login(String name, String password, ClientType type)
			throws LoginException, CouponSystemException;

}
