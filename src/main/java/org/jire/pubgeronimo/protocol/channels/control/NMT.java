package org.jire.pubgeronimo.protocol.channels.control;

public enum NMT {
	
	HELLO(0),
	WELCOME(1),
	UPGRADE(2),
	CHALLENGE(3),
	NETSPEED(4),
	LOGIN(5),
	FAILURE(6),
	UNKNOWN_7(7),
	UNKNOWN_8(8),
	JOIN(9),
	JOIN_SPLIT(10),
	UNKNOWN_11(11),
	SKIP(12),
	ABORT(13),
	UNKNOWN_14(14),
	PC_SWAP(15),
	ACTOR_CHANNEL_FAILURE(16),
	DEBUG_TEXT(17),
	NET_GUID_ASSIGN(18),
	UNKNOWN_19(19),
	UNKNOWN_20(20),
	ENCRYPTION_ACK(21),
	UNKNOWN_22(22),
	UNKNOWN_23(23),
	UNKNOWN_24(24),
	BEACON_WELCOME(25),
	BEACON_JOIN(26),
	BEACON_ASSIGN_GUID(27),
	BEACON_NET_GUID_ACK(28);
	
	private final int value;
	
	NMT(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static final NMT[] cachedValues = NMT.values();
	
}
