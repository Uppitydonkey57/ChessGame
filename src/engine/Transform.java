package engine;

import org.joml.*;
import java.lang.Math;

public class Transform {
	public float rotation;
	public Vector2f position;
	public Vector2f scale;
	public float order;

	public Transform(Vector2f position, float rotation, Vector2f scale, float order) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.order = order;
	}

	public Matrix4f getTransformMatrix() {
		Matrix4f model = new Matrix4f().identity();
		model.translate(position.x, position.y, order);
		model.rotate(new AxisAngle4f((float) Math.toRadians(rotation), 0, 0, 1)); // TODO CONVERT TO RADIANS!!!
		model.scale(scale.x, scale.y, 1);
		return model;
	}
}