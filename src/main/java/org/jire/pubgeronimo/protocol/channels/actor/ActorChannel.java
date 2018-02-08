package org.jire.pubgeronimo.protocol.channels.actor;

import org.jire.pubgeronimo.*;
import org.jire.pubgeronimo.protocol.channels.Channel;
import org.jire.pubgeronimo.protocol.channels.ChannelType;

public final class ActorChannel extends Channel {
	
	private volatile boolean serialized;
	
	public ActorChannel(int channelIndex) {
		super(channelIndex, ChannelType.ACTOR);
	}
	
	@Override
	public void decode(PUBGBuffer buffer) {
		if (!serialized) {
			final ObjectPtr objectPtr = buffer.readObject();
			if (objectPtr != null) {
				final NetworkGUID typeNetGUID = objectPtr.getNetworkGUID();
				final NetGUIDObject type = objectPtr.getNetGUIDObject();
				final String pathName = type.getPathName();
				System.out.println(pathName + " (" + ActorType.typeForString(pathName) + ")");
				
				final boolean bSerializeLocation = buffer.readBit();
				
				final Vector3 location = bSerializeLocation ? buffer.readVector() : new Vector3(0, 0, 0);
				System.out.println("at " + location);
				
				final boolean bSerializeRotation = buffer.readBit();
				final Vector3 rotation = bSerializeRotation ? buffer.readRotationShort() : new Vector3(0, 0, 0);
				
				final boolean bSerializeScale = buffer.readBit();
				final Vector3 scale = bSerializeScale ? buffer.readVector() : new Vector3(0, 0, 0);
				
				final boolean bSerializeVelocity = buffer.readBit();
				final Vector3 velocity = bSerializeVelocity ? buffer.readVector() : new Vector3(0, 0, 0);
				
			}
			
			//serialized = true;
		}
		
		/*while (buffer.notEnd()) {
			final boolean bHasRepLayout = buffer.readBit();
			final boolean bIsActor = buffer.readBit();
			if (!bIsActor) {
				final ObjectPtr objectPtr = buffer.readObject();
				if (objectPtr != null) {
					final NetworkGUID netguid = objectPtr.getNetworkGUID();
					final NetGUIDObject subobj = objectPtr.getNetGUIDObject();
					
					final boolean bStablyNamed = buffer.readBit();
					if (bStablyNamed) { // if the subobject is stably named, don't need to create it
						if (subobj == null) continue;
					} else {
						final ObjectPtr subObjectPtr = buffer.readObject();
						if (subObjectPtr != null) {
							final NetworkGUID classGUID = subObjectPtr.getNetworkGUID();
							final NetGUIDObject classObj = subObjectPtr.getNetGUIDObject();
							if (classObj != null *//* && actor.Type == DroopedItemGroup*//*) {
							
							}
						}
					}
				}
			}
			
			final int NumPayloadBits = buffer.readIntPacked();
			if (NumPayloadBits < 0 || NumPayloadBits > buffer.bitsLeft()) return;
			
			buffer.skipBits(NumPayloadBits);
		}*/
	}
	
}
