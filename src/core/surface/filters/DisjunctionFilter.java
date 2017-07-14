package core.surface.filters;

import core.surface.Surface;

public class DisjunctionFilter extends Filter {
	
	private FilterSet filters;
	
	public DisjunctionFilter() {
		filters = new FilterSet();
	}
	
	public void addFilter(Filter f) {
		filters.add(f);
	}

	@Override
	boolean accepts(Surface surface) {
		for(Filter f : filters)
			if(f.accepts(surface))
				return true;
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!super.equals(other))
			return false;
		DisjunctionFilter o = (DisjunctionFilter) other;
		return filters.equals(o.filters);
	}
	
	@Override
	public int hashCode() {
		return filters.hashCode();
	}

}
