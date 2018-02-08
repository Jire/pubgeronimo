package org.jire.pubgeronimo;

import org.jire.pubgeronimo.sniffer.Sniffer;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle.BlockingMode;
import org.pcap4j.core.PcapNativeException;

public final class PUBGeronimo {
	
	private static final String BPF_EXPRESSION = "udp src portrange 7000-8999 or udp[4:2] = 52";
	
	public static void main(String[] args) {
		final Sniffer sniffer = new Sniffer();
		try {
			sniffer.determineNetworkInterface(0);
		} catch (PcapNativeException e) {
			e.printStackTrace();
			return;
		}
		sniffer.determineLocalAddress();
		try {
			sniffer.sniff(BPF_EXPRESSION);
			sniffer.getHandle().setBlockingMode(BlockingMode.BLOCKING);
			
			final Thread snifferThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						sniffer.getHandle().loop(-1, new PUBGSniffer(sniffer));
					} catch (PcapNativeException e) {
						e.printStackTrace();
						System.exit(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(1);
					} catch (NotOpenException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			});
			snifferThread.setDaemon(false);
			snifferThread.start();
		} catch (PcapNativeException e) {
			e.printStackTrace();
			return;
		} catch (NotOpenException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Sniffing (\"" + BPF_EXPRESSION + "\") on device "
				+ sniffer.getNetworkInterface().getDescription());
	}
	
}
