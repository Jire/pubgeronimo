package org.jire.pubgeronimo;

import org.jire.pubgeronimo.protocol.Constants;
import org.jire.pubgeronimo.protocol.channels.Channel;
import org.jire.pubgeronimo.protocol.channels.ChannelType;
import org.jire.pubgeronimo.protocol.channels.actor.ActorChannel;
import org.jire.pubgeronimo.protocol.channels.control.ControlChannel;
import org.jire.pubgeronimo.sniffer.Sniffer;
import org.jire.pubgeronimo.sniffer.SnifferListener;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;

public final class PUBGSniffer extends SnifferListener {
	
	private final Channel[] channels = new Channel[Constants.MAX_CHANNELS];
	
	@Override
	public void sniff(Sniffer sniffer, Packet packet) {
		final IpPacket ip = packet.get(IpPacket.class);
		final UdpPacket udp = packet.get(UdpPacket.class);
		final byte[] raw = udp.getPayload().getRawData();
		final int sourcePort = udp.getHeader().getSrcPort().valueAsInt();
		if (getLocalAddress().equals(ip.getHeader().getSrcAddr())) {
			if (raw.length == 44) {
				// parse self location
				//System.out.println("self location");
			}
		} else if (sourcePort >= 7000 && sourcePort <= 7999) {
			final PUBGBuffer buffer = new PUBGBuffer(raw);
			processRawPacket(buffer);
		}
	}
	
	private void processRawPacket(PUBGBuffer buffer) {
		if (buffer.readBit()) return; // handshake bit
		if (buffer.readBit()) return; // encrypted bit
		
		final int packetID = buffer.readInt(PUBGBuffer.MAX_PACKET_ID);
		while (buffer.notEnd()) {
			final boolean isAck = buffer.readBit();
			if (isAck) {
				final int ackPacketID = buffer.readInt(PUBGBuffer.MAX_PACKET_ID);
				if (ackPacketID == -1) return;
				final boolean bHasServerFrameTime = buffer.readBit();
				final int remoteInKBytesPerSecond = buffer.readIntPacked();
				continue;
			}
			
			final boolean bControl = buffer.readBit();
			boolean bOpen = false, bClose, bDormant;
			if (bControl) {
				bOpen = buffer.readBit();
				bClose = buffer.readBit();
				if (bClose) bDormant = buffer.readBit();
			}
			final boolean bIsReplicationPaused = buffer.readBit();
			final boolean bReliable = buffer.readBit();
			final int chIndex = buffer.readInt(PUBGBuffer.MAX_CHANNELS);
			if (chIndex < 0 || chIndex >= channels.length) continue;
			final boolean bHasPackageMapExports = buffer.readBit();
			final boolean bHasMustBeMappedGUIDs = buffer.readBit();
			final boolean bPartial = buffer.readBit();
			final int chSequence
					= bReliable ? buffer.readInt(PUBGBuffer.MAX_CH_SEQUENCE)
					: bPartial ? packetID : 0;
			boolean bPartialInitial, bPartialFinal;
			if (bPartial) {
				bPartialInitial = buffer.readBit();
				bPartialFinal = buffer.readBit();
			}
			
			final int chType = (bReliable || bOpen)
					? buffer.readInt(PUBGBuffer.CHTYPE_MAX) : PUBGBuffer.CHTYPE_NONE;
			if (chType != ChannelType.CONTROL.getValue()
					&& chType != ChannelType.ACTOR.getValue()) return; // optimize away
			//if (chType < 0 || chType >= ChannelType.cachedValues.length) return;
			
			final int bunchDataBits = buffer.readInt(PUBGBuffer.MAX_PACKET_SIZE * 8);
			final int pre = buffer.bitsLeft();
			if (bunchDataBits > pre) return;
			
			final ChannelType channelType = ChannelType.cachedValues[chType];
			
			Channel channel = channels[chIndex];
			if (channel == null) {
				channels[chIndex] = channel = channelType == ChannelType.CONTROL ?
						new ControlChannel(chIndex) : new ActorChannel(chIndex);
				//System.out.println("Made new channel (" + chIndex + "): " + channel.getClass().getName());
			}
			
			if (bHasMustBeMappedGUIDs) {
				final int numMustBeMappedGUIDs = buffer.readUInt16();
				for (int i = 0; i < numMustBeMappedGUIDs; i++) {
					final NetworkGUID networkGUID = buffer.readNetworkGUID();
				}
			}
			
			channel.decode(buffer);
		}
	}
	
	public PUBGSniffer(Sniffer sniffer) {
		super(sniffer);
	}
	
}
