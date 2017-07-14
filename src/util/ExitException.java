package util;

public class ExitException extends SecurityException {

	private static final long serialVersionUID = 1L;
	private int exitStatus;

	public ExitException(int status) {
		super();
		exitStatus = status;
	}

	public int getExitStatus() {
		return exitStatus;
	}

}
