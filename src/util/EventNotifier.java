package util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.EventObject;

public class EventNotifier<E extends EventObject> {
	
	private Collection<EventListener<E>> listeners;
	
	public EventNotifier() {
		listeners = new LinkedList<EventListener<E>>();
	}
	
	public void addEventListener(EventListener<E> listener) {
		listeners.add(listener);
	}
	
	public void removeEventListener(EventListener<E> listener) {
		listeners.remove(listener);
	}
	
	protected void notifyEvent(E e) {
		for(EventListener<E> listener : listeners)
			listener.eventOccurred(e);
	}
}
