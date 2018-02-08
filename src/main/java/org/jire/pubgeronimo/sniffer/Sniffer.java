package org.jire.pubgeronimo.sniffer;

import org.pcap4j.core.*;

import java.net.Inet4Address;
import java.net.InetAddress;

import static org.jire.pubgeronimo.sniffer.SnifferSettings.*;

public final class Sniffer implements SnifferInformation {
	
	private PcapNetworkInterface networkInterface;
	private InetAddress localAddress;
	private PcapHandle handle;
	
	public void determineNetworkInterface(int networkInterfaceIndex)
			throws PcapNativeException, IndexOutOfBoundsException {
		networkInterface = Pcaps.findAllDevs().get(networkInterfaceIndex);
	}
	
	public void determineLocalAddress() {
		for (PcapAddress address : networkInterface.getAddresses()) {
			if (address != null && address.getAddress() instanceof Inet4Address) {
				localAddress = address.getAddress();
				break;
			}
		}
	}
	
	public void sniff(String bpfExpression) throws PcapNativeException, NotOpenException {
		handle = networkInterface.openLive(SNAP_LENGTH, PROMISCUOUS_MODE, TIMEOUT);
		handle.setFilter(bpfExpression, BPF_COMPILE_MODE);
	}
	
	@Override
	public PcapNetworkInterface getNetworkInterface() {
		return networkInterface;
	}
	
	@Override
	public InetAddress getLocalAddress() {
		return localAddress;
	}
	
	@Override
	public PcapHandle getHandle() {
		return handle;
	}
	
}
