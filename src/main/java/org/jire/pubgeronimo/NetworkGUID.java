package org.jire.pubgeronimo;

public final class NetworkGUID {
	
	private final int value;
	
	public NetworkGUID(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isDynamic() {
		return value > 0 && (value & 1) == 0;
	}
	
	public boolean isValid() {
		return value > 0;
	}
	
	public boolean isStatic() {
		return (value & 1) == 0;
	}
	
	public boolean isDefault() {
		return value == 1;
	}
	
}
