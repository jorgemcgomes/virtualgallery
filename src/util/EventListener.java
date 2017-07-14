package util;

import java.util.EventObject;

public interface EventListener<E extends EventObject>  {
	
	public void eventOccurred(E e);

}
