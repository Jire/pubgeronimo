package org.jire.pubgeronimo.protocol.channels.control;

import org.jire.pubgeronimo.Bunch;
import org.jire.pubgeronimo.PUBGBuffer;
import org.jire.pubgeronimo.protocol.channels.Channel;
import org.jire.pubgeronimo.protocol.channels.ChannelType;

public final class ControlChannel extends Channel {
	
	public ControlChannel(int channelIndex) {
		super(channelIndex, ChannelType.CONTROL);
	}
	
	@Override
	public void decode(Bunch bunch) {
		final int typeValue = bunch.readUInt8();
		final NMT type = NMT.cachedValues[typeValue];
		System.out.println(type);
		if (type == NMT.WELCOME) {
			final String map = bunch.readString();
			final String gameMode = bunch.readString();
			final String unknown = bunch.readString();
			System.out.println("map=" + map + ", gameMode=" + gameMode);
		}
	}
	
}
