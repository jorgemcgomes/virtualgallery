package util;

import java.security.Permission;

public class NoExitSecurityManager extends SecurityManager {
	
	private static SecurityManager securityManager;

	@Override
	public void checkPermission(Permission perm) {
		// allow anything.
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		// allow anything.
	}

	@Override
	public void checkExit(int status) {
		super.checkExit(status);
		throw new ExitException(status);
	}

	public static void forbidExit() {
		securityManager = System.getSecurityManager();
		System.setSecurityManager(new NoExitSecurityManager());
	}

	public static void enableExit() {
		System.setSecurityManager(securityManager);
	}

}
