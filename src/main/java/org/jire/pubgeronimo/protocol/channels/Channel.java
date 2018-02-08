package org.jire.pubgeronimo.protocol.channels;

import org.jire.pubgeronimo.protocol.Decoder;

public abstract class Channel implements Decoder {
	
	private final int channelIndex;
	private final ChannelType chType;
	
	private boolean bDormant;
	private boolean CLosing;
	private boolean InReliable;
	private int numInRec;
	
	public Channel(int channelIndex, ChannelType chType) {
		this.channelIndex = channelIndex;
		this.chType = chType;
	}
	
	public int getChannelIndex() {
		return channelIndex;
	}
	
	public ChannelType getChannelType() {
		return chType;
	}
	
}
