package core.surface;

import java.util.Collection;

import core.surface.filters.FilterSet;


class AutoPicker {
	
	private Collection<SurfaceGroup> surfaces;
	
	AutoPicker(Collection<SurfaceGroup> surfaces) {
		this.surfaces = surfaces;
	}
	
	int autoSelect(FilterSet filters) {
		int count = 0;
		for(SurfaceGroup sg : surfaces)
			for(Surface s : sg)
				if(!s.isSelectable() && filters.passFilters(s)) {
					s.setSelectable(true);
					s.highlight();
					count++;
				}
		return count;
	}
	
	int autoDeselect(FilterSet filters) {
		int count = 0;
		for(SurfaceGroup sg : surfaces)
			for(Surface s : sg)
				if(s.isSelectable() && filters.passFilters(s)) {
					s.setSelectable(false);
					s.invisible();
					count++;
				}
		return count;
	}
	
	void clearSelection() {
		for(SurfaceGroup sg: surfaces)
			for(Surface s : sg) {
				s.setSelectable(false);
				s.invisible();
			}
	}
	
	void selectAll() {
		for(SurfaceGroup sg : surfaces)
			for(Surface s : sg) {
				s.setSelectable(true);
				s.highlight();
			}
	}
}
