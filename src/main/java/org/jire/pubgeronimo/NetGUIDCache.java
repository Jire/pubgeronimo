package org.jire.pubgeronimo;

import java.util.HashMap;
import java.util.Map;

public final class NetGUIDCache {
	
	public static final NetGUIDCache INSTANCE = new NetGUIDCache();
	
	private final Map<NetworkGUID, NetGUIDObject> objectLoop = new HashMap<NetworkGUID, NetGUIDObject>();
	private boolean isExportingNetGUIDBunch = false;
	
	public boolean isExportingNetGUIDBunch() {
		return isExportingNetGUIDBunch;
	}
	
	public void setExportingNetGUIDBunch(boolean exportingNetGUIDBunch) {
		isExportingNetGUIDBunch = exportingNetGUIDBunch;
	}
	
	public NetGUIDObject get(int index) {
		return objectLoop.get(new NetworkGUID(index));
	}
	
	public NetGUIDObject getObjectFromNetGUID(NetworkGUID netGUID) {
		final NetGUIDObject cacheObject = objectLoop.get(netGUID);
		if (cacheObject == null || cacheObject.getPathName().isEmpty()) return null;
		return cacheObject;
	}
	
	public void registerNetGUIDFromPath_Client(NetworkGUID netGUID, String pathName, NetworkGUID outerGUID,
	                                           int networkChecksum, boolean bNoLoad, boolean bIgnoreWhenMissing) {
		if (objectLoop.get(netGUID) == null)
			objectLoop.put(netGUID, new NetGUIDObject(pathName, outerGUID, networkChecksum, bNoLoad, bIgnoreWhenMissing));
	}
	
	public void registerNetGUID_Client(NetworkGUID netGUID, Object obj) {
		//if (objectLoop.get(netGUID) != null) objectLoop.remove(netGUID);
		objectLoop.put(netGUID, new NetGUIDObject("", netGUID));
	}
	
}
