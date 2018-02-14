package org.jire.pubgeronimo.protocol.channels.actor;

import org.jire.pubgeronimo.*;
import org.jire.pubgeronimo.protocol.channels.Channel;
import org.jire.pubgeronimo.protocol.channels.ChannelType;

public final class ActorChannel extends Channel {
	
	private volatile Actor actor = null;
	
	public ActorChannel(int channelIndex) {
		super(channelIndex, ChannelType.ACTOR);
	}
	
	@Override
	public void decode(Bunch bunch) {
		if (bunch.bHasMustBeMappedGUIDs) {
			final int NumMustBeMappedGUIDs = bunch.readUInt16();
			for (int i = 0; i < NumMustBeMappedGUIDs; i++) {
				final NetworkGUID guid = bunch.readNetworkGUID();
			}
		}
		
		if (actor == null) {
			if (!bunch.bOpen) return;
			serializeActor(bunch);
			if (actor == null) return;
		}
		
		while (bunch.notEnd()) {
			final boolean bHasRepLayout = bunch.readBit();
			final boolean bIsActor = bunch.readBit();
			if (!bIsActor) {
				final ObjectPtr objectPtr = bunch.readObject();
				if (objectPtr != null) {
					final NetworkGUID netguid = objectPtr.getNetworkGUID();
					final NetGUIDObject subobj = objectPtr.getNetGUIDObject();
					
					final boolean bStablyNamed = bunch.readBit();
					if (bStablyNamed) { // if the subobject is stably named, don't need to create it
						if (subobj == null) continue;
					} else {
						final ObjectPtr subObjectPtr = bunch.readObject();
						if (subObjectPtr != null) {
							final NetworkGUID classGUID = subObjectPtr.getNetworkGUID();
							final NetGUIDObject classObj = subObjectPtr.getNetGUIDObject();
							if (classObj != null/* && actor.Type == DroopedItemGroup*/) {
								System.out.println("thing!! " + classObj.getPathName());
							}
						}
					}
				}
			}
			
			final int NumPayloadBits = bunch.readIntPacked();
			if (NumPayloadBits < 0 || NumPayloadBits > bunch.bitsLeft()) return;
			
			bunch.skipBits(NumPayloadBits);
		}
	}
	
	private void serializeActor(Bunch bunch) {
		final ObjectPtr objectPtr = bunch.readObject();
		if (objectPtr != null) {
			final NetworkGUID netGUID = objectPtr.getNetworkGUID();
			final NetGUIDObject newActor = objectPtr.getNetGUIDObject();
			if (netGUID.isDynamic()) {
				final ObjectPtr archetypeObjectPtr = bunch.readObject();
				if (archetypeObjectPtr != null) {
					final NetworkGUID archetypeNetGUID = objectPtr.getNetworkGUID();
					final NetGUIDObject archetype = objectPtr.getNetGUIDObject();
					final String pathName = archetype.getPathName();
					System.out.println(pathName + " (" + ActorType.typeForString(pathName) + ")");
					
					final boolean bSerializeLocation = bunch.readBit();
					
					final Vector3 location = bSerializeLocation ? bunch.readVector() : new Vector3(0, 0, 0);
					System.out.println("at " + location);
					
					final boolean bSerializeRotation = bunch.readBit();
					final Vector3 rotation = bSerializeRotation ? bunch.readRotationShort() : new Vector3(0, 0, 0);
					
					final boolean bSerializeScale = bunch.readBit();
					final Vector3 scale = bSerializeScale ? bunch.readVector() : new Vector3(0, 0, 0);
					
					final boolean bSerializeVelocity = bunch.readBit();
					final Vector3 velocity = bSerializeVelocity ? bunch.readVector() : new Vector3(0, 0, 0);
					
					actor = new Actor(netGUID, archetypeNetGUID, archetype, getChannelIndex());
					actor.setLocation(location);
					actor.setRotation(rotation);
					actor.setVelocity(velocity);
				}
			} else {
				if (newActor == null) return;
				actor = new Actor(netGUID, newActor.getOuterGUID(), newActor, getChannelIndex());
				actor.setStatic(true);
			}
		}
	}
	
}
