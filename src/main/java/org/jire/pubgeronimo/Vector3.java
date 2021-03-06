package org.jire.pubgeronimo;

public final class Vector3 {
	
	public static final Vector3 Zero = new Vector3(0F, 0F, 0F);
	
	private final float x, y, z;
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
}
