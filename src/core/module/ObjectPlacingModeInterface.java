package core.module;

public interface ObjectPlacingModeInterface extends DirectionConstants {

	void rotateSelected(int direction);
	void translateSelected(int direction);
	void resetSelected();
	void deleteSelected();
	void setCoarse();
	void setFine();
}
