/**
 *
 */
package de.neuenberger.games.core.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Michael Kirchmann, PRODYNA AG
 * @date 23.04.2013
 */
public class ControllablePosition {
	Vector3	position		= new Vector3();
	float	rotationY;
	float	rotationX;
	Matrix4	transformation	= new Matrix4();
	Vector3	vector			= new Vector3();

	/**
	 * @param input
	 */
	public void control(Input input) {
		float faster = 1.0f;
		if (input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			faster = 0.1f;
		}
		float acc = faster * 40 * Gdx.graphics.getDeltaTime();
		boolean changed = false;

		vector.x = 0;
		vector.y = 0;
		vector.z = 0;
		if (input.isKeyPressed(Input.Keys.UP)) {
			vector.z = acc;
			changed = true;
		}
		if (input.isKeyPressed(Input.Keys.DOWN)) {
			vector.z = -acc;
			changed = true;
		}

		if (input.isKeyPressed(Input.Keys.LEFT)) {
			vector.x = acc;
			changed = true;
		}
		if (input.isKeyPressed(Input.Keys.RIGHT)) {
			vector.x = -acc;
			changed = true;
		}
		if (input.isKeyPressed(Input.Keys.Q)) {
			vector.y = -acc;
			changed = true;
		}
		if (input.isKeyPressed(Input.Keys.W)) {
			vector.y = acc;
			changed = true;
		}
		if (input.isKeyPressed(Input.Keys.E)) {
			rotationY += acc;
		}
		if (input.isKeyPressed(Input.Keys.R)) {
			rotationY -= acc;
		}
		if (input.isKeyPressed(Input.Keys.D)) {
			rotationX += acc;
		}
		if (input.isKeyPressed(Input.Keys.F)) {
			rotationX -= acc;
		}
		if (changed) {
			transformation.idt();
			transformation.rotate(0, 1, 0, rotationY);
			transformation.rotate(1, 0, 0, getRotationX());
			vector.mul(transformation);
			position.add(vector);
			vector.x = 0;
			vector.y = 0;
			vector.z = 0;
		}

		if (changed) {
			System.out.println("Position is now: " + position);
		}
	}

	/**
	 * @return the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	public float getRotationY() {
		return rotationY;
	}

	public float getRotationX() {
		return rotationX;
	}

	/**
	 * @param camera
	 */
	public void apply(Camera camera) {
		camera.position.set(0, 0, 0);
		camera.direction.set(0, 0, 1);

		camera.rotate(getRotationY(), 0, 1, 0);
		camera.rotate(getRotationX(), 1, 0, 0);
		camera.translate(0, 2, 0);
		camera.translate(getPosition());
		camera.up.set(0, 1, 0);
		camera.update();
	}

}
