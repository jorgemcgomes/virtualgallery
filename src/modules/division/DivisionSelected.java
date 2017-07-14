package modules.division;

import java.util.EventObject;

class DivisionSelected extends EventObject {

	private Division div;
	
	public DivisionSelected(Object src, Division div) {
		super(src);
		this.div = div;
	}
	
	public Division getSelected() {
		return div;
	}

}
