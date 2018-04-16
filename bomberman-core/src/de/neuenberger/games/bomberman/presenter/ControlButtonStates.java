package de.neuenberger.games.bomberman.presenter;

public class ControlButtonStates {
	boolean upButtonPressed;
	boolean downButtonPressed;
	boolean leftButtonPressed;
	boolean rightButtonPressed;
	boolean fireButtonPressed;

	public boolean isUpButtonPressed() {
		return upButtonPressed;
	}

	public void setUpButtonPressed(boolean upButtonPressed) {
		this.upButtonPressed = upButtonPressed;
	}

	public boolean isDownButtonPressed() {
		return downButtonPressed;
	}

	public void setDownButtonPressed(boolean downButtonPressed) {
		this.downButtonPressed = downButtonPressed;
	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public void setLeftButtonPressed(boolean leftButtonPressed) {
		this.leftButtonPressed = leftButtonPressed;
	}

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}

	public void setRightButtonPressed(boolean rightButtonPressed) {
		this.rightButtonPressed = rightButtonPressed;
	}

	public boolean isFireButtonPressed() {
		return fireButtonPressed;
	}

	public void setFireButtonPressed(boolean fireButtonPressed) {
		this.fireButtonPressed = fireButtonPressed;
	}

	@Override
	public String toString() {
		return "ControlButtonStates [upButtonPressed=" + upButtonPressed + ", downButtonPressed=" + downButtonPressed
				+ ", leftButtonPressed=" + leftButtonPressed + ", rightButtonPressed=" + rightButtonPressed
				+ ", fireButtonPressed=" + fireButtonPressed + "]";
	}

}
