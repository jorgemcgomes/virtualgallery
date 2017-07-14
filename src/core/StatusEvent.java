package core;

import java.util.EventObject;

public class StatusEvent extends EventObject {
	
	public static final int ERROR_MSG = -1, WARNING_MSG = 0, OK_MSG = 1;
	
	private int type;
	private String message;
	
	public StatusEvent(Object src, int type, String message) {
		super(src);
		this.type = type;
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
