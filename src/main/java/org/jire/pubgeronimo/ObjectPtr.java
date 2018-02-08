package org.jire.pubgeronimo;

public final class ObjectPtr {
	
	private final NetworkGUID networkGUID;
	private final NetGUIDObject netGUIDObject;
	
	public ObjectPtr(NetworkGUID networkGUID, NetGUIDObject netGUIDObject) {
		this.networkGUID = networkGUID;
		this.netGUIDObject = netGUIDObject;
	}
	
	public NetworkGUID getNetworkGUID() {
		return networkGUID;
	}
	
	public NetGUIDObject getNetGUIDObject() {
		return netGUIDObject;
	}
	
}
