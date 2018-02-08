package org.jire.pubgeronimo.sniffer;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;

import java.net.InetAddress;

public interface SnifferInformation {
	
	PcapNetworkInterface getNetworkInterface();
	
	InetAddress getLocalAddress();
	
	PcapHandle getHandle();
	
}
