package core.surface.filters;

import core.surface.Surface;

abstract class Filter {

	abstract boolean accepts(Surface surface);
	
	@Override
	public boolean equals(Object other) {
		if(this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
