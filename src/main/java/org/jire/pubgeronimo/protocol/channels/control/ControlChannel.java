package org.jire.pubgeronimo.protocol.channels.control;

import org.jire.pubgeronimo.PUBGBuffer;
import org.jire.pubgeronimo.protocol.channels.Channel;
import org.jire.pubgeronimo.protocol.channels.ChannelType;

public final class ControlChannel extends Channel {
	
	public ControlChannel(int channelIndex) {
		super(channelIndex, ChannelType.CONTROL);
	}
	
	@Override
	public void decode(PUBGBuffer buffer) {
		final int typeValue = buffer.readUInt8();
		final NMT type = NMT.cachedValues[typeValue];
		System.out.println(type);
		if (type == NMT.WELCOME) {
			final String map = buffer.readString();
			final String gameMode = buffer.readString();
			final String unknown = buffer.readString();
			System.out.println("map=" + map + ", gameMode=" + gameMode);
		}
	}
	
}
