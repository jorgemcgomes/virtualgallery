package core.surface.filters;

import javax.vecmath.Vector3f;

import util.Utils;

import core.surface.Surface;


public class NormalFilter extends Filter {

	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	public static final int OBLIQUE = 3;
	public static final int ZERO = 0;
	public static final int NON_ZERO = -1;
	public static final int ALL = 1;

	private int xRestriction, yRestriction, zRestriction;

	public NormalFilter(int x, int y, int z) {
		this.xRestriction = x;
		this.yRestriction = y;
		this.zRestriction = z;
	}

	public NormalFilter(int direction) {
		switch(direction) {
		case HORIZONTAL:
			xRestriction = ZERO;
			yRestriction = NON_ZERO;
			zRestriction = ZERO;
			break;
		case VERTICAL:
			xRestriction = ALL;
			yRestriction = ZERO;
			zRestriction = ALL;
			break;
		case OBLIQUE:
			xRestriction = ALL;
			yRestriction = NON_ZERO;
			zRestriction = ALL;
			break;
		}
	}

	@Override
	boolean accepts(Surface surface) {
		Vector3f normal = surface.getNormal();
		boolean res = componentAccepted(xRestriction, normal.x) &&
						componentAccepted(yRestriction, normal.y) &&
						componentAccepted(zRestriction, normal.z);
		return res;
	}

	private boolean componentAccepted(int restriction, float component) {
		boolean res = true;
		switch(restriction) {
		case ZERO:
			res = Utils.isZero(component);
			break;
		case NON_ZERO:
			res = !Utils.isZero(component);
			break;
		case ALL:
			res = true;
			break;
		default:
			res = false; // TODO ou true?
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + xRestriction;
		result = prime * result + yRestriction;
		result = prime * result + zRestriction;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		NormalFilter other = (NormalFilter) obj;
		if (xRestriction != other.xRestriction)
			return false;
		if (yRestriction != other.yRestriction)
			return false;
		if (zRestriction != other.zRestriction)
			return false;
		return true;
	}
	


}
