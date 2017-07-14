package core.surface.filters;

import java.util.HashSet;

import core.surface.Surface;


public class FilterSet extends HashSet<Filter> {
	
	private static final long serialVersionUID = 1L;

	public FilterSet() {
		super(5);
	}
	
	public boolean passFilters(Surface s) {
		for(Filter f : this)
			if(!f.accepts(s))
				return false;
		return true;
	}
	
	@Override
	public boolean add(Filter f) {
		if(contains(f))
			remove(f);
		super.add(f);
		return true;
	}

}
