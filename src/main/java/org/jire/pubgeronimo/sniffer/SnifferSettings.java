package org.jire.pubgeronimo.sniffer;

import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;

public final class SnifferSettings {
	
	public static final int SNAP_LENGTH = 65536;
	public static final PromiscuousMode PROMISCUOUS_MODE = PromiscuousMode.PROMISCUOUS;
	public static final int TIMEOUT = 1;
	public static final BpfCompileMode BPF_COMPILE_MODE = BpfCompileMode.OPTIMIZE;
	
}
