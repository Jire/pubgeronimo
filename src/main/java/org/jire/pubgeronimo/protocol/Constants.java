package org.jire.pubgeronimo.protocol;

public final class Constants {
	
	public static final int RELIABLE_BUFFER = 256;
	public static final int MAX_PACKET_ID = 16384;
	public static final int MAX_CHANNELS = 10240;
	public static final int MAX_CHSEQUENCE = 1024;
	public static final int MAX_BUNCH_HEADER_BITS = 64;
	public static final int NAME_SIZE = 1024;
	public static final int MAX_NETWORKED_HARDCODED_NAME = 410;
	
	public static final int ROLE_MAX = 4;
	
	public static final int MAX_PACKET_SIZE = 1228;
	
	private Constants() {
		throw new UnsupportedOperationException();
	}
	
}
