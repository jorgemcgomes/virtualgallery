package core.surface;

import core.surface.filters.FilterSet;
import util.EventListener;

interface SurfaceSelectionInterface {
	
	int autoPick(FilterSet filters);
	int autoReject(FilterSet filters);
	void pickAll();
	void clearPicks();
	void addSurfaceListener(EventListener<SurfaceEvent> listener);
	void activate();
	void terminate();
	
}
