package org.jire.pubgeronimo.sniffer;

import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.packet.Packet;

import java.net.InetAddress;

public abstract class SnifferListener implements PacketListener, SnifferInformation {
	
	private final Sniffer sniffer;
	
	public SnifferListener(Sniffer sniffer) {
		this.sniffer = sniffer;
	}
	
	public abstract void sniff(Sniffer sniffer, Packet packet) throws Exception;
	
	@Override
	public void gotPacket(Packet packet) {
		if (packet != null) {
			try {
				sniff(sniffer, packet);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	public PcapNetworkInterface getNetworkInterface() {
		return sniffer.getNetworkInterface();
	}
	
	@Override
	public InetAddress getLocalAddress() {
		return sniffer.getLocalAddress();
	}
	
	@Override
	public PcapHandle getHandle() {
		return sniffer.getHandle();
	}
	
}
