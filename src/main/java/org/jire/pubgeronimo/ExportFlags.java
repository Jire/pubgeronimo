package org.jire.pubgeronimo;

public final class ExportFlags {
	
	private final int value;
	
	public ExportFlags(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean bHasPath() {
		return 0 != (value & 1);
	}
	
	public boolean bNoLoad() {
		return 0 != (value & 2);
	}
	
	public boolean bHasNetworkChecksum() {
		return 0 != (value & 4);
	}
	
}
