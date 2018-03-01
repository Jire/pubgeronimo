package org.jire.pubgeronimo;

public final class NetGUIDObject {
	
	private final String pathName;
	private final NetworkGUID outerGUID;
	private final int networkChecksum;
	private final boolean bNoLoad;
	private final boolean IgnoreWhenMissing;
	
	private Object holdObj = null;
	
	public NetGUIDObject(String pathName, NetworkGUID outerGUID, int networkChecksum,
	                     boolean bNoLoad, boolean ignoreWhenMissing) {
		this.pathName = pathName;
		this.outerGUID = outerGUID;
		this.networkChecksum = networkChecksum;
		this.bNoLoad = bNoLoad;
		IgnoreWhenMissing = ignoreWhenMissing;
	}
	
	public NetGUIDObject(String pathName, NetworkGUID outerGUID) {
		this(pathName, outerGUID, 0, false, false);
	}
	
	public String getPathName() {
		return pathName;
	}
	
	public NetworkGUID getOuterGUID() {
		return outerGUID;
	}
	
	public int getNetworkChecksum() {
		return networkChecksum;
	}
	
	public boolean isbNoLoad() {
		return bNoLoad;
	}
	
	public boolean isIgnoreWhenMissing() {
		return IgnoreWhenMissing;
	}
	
	public Object getHoldObj() {
		return holdObj;
	}
	
	public void setHoldObj(Object holdObj) {
		this.holdObj = holdObj;
	}
	
}
