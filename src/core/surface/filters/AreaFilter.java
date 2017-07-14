package core.surface.filters;

import core.surface.Surface;

public class AreaFilter extends Filter {
	
	private float minimumArea;
	
	public AreaFilter(float minimumArea) {
		this.minimumArea = minimumArea;
	}

	@Override
	boolean accepts(Surface surface) {
		float area = surface.area();
		return area >= minimumArea;
	} 

}
