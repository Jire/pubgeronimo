package org.jire.pubgeronimo.protocol.channels;

public enum ChannelType {
	
	NONE(0),
	CONTROL(1),
	ACTOR(2),
	FILE(3),
	VOICE(4),
	UNKNOWN_5(5),
	UNKNOWN_6(6),
	UNKNOWN_7(7),
	MAX(8);
	
	private final int value;
	
	ChannelType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static final ChannelType[] cachedValues = ChannelType.values();
	
}
