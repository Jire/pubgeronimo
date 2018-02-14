package org.jire.pubgeronimo.protocol.channels.actor;

import org.jire.pubgeronimo.NetGUIDObject;
import org.jire.pubgeronimo.NetworkGUID;
import org.jire.pubgeronimo.Vector3;

public final class Actor {

	private final NetworkGUID netGUID;
	private final NetworkGUID archetypeGUID;
	private final NetGUIDObject archetype;
	private final int ChIndex;
	
	private Vector3 location = Vector3.Zero;
	private Vector3 rotation = Vector3.Zero;
	private Vector3 velocity = Vector3.Zero;
	
	private NetworkGUID owner = null;
	private NetworkGUID attachTo = null;
	private boolean beAttached = false;
	private boolean isStatic = false;
	
	public Actor(NetworkGUID netGUID, NetworkGUID archetypeGUID, NetGUIDObject archetype, int chIndex) {
		this.netGUID = netGUID;
		this.archetypeGUID = archetypeGUID;
		this.archetype = archetype;
		ChIndex = chIndex;
	}
	
	public NetworkGUID getNetGUID() {
		return netGUID;
	}
	
	public NetworkGUID getArchetypeGUID() {
		return archetypeGUID;
	}
	
	public NetGUIDObject getArchetype() {
		return archetype;
	}
	
	public int getChIndex() {
		return ChIndex;
	}
	
	public Vector3 getLocation() {
		return location;
	}
	
	public void setLocation(Vector3 location) {
		this.location = location;
	}
	
	public Vector3 getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}
	
	public Vector3 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}
	
	public NetworkGUID getOwner() {
		return owner;
	}
	
	public void setOwner(NetworkGUID owner) {
		this.owner = owner;
	}
	
	public NetworkGUID getAttachTo() {
		return attachTo;
	}
	
	public void setAttachTo(NetworkGUID attachTo) {
		this.attachTo = attachTo;
	}
	
	public boolean isBeAttached() {
		return beAttached;
	}
	
	public void setBeAttached(boolean beAttached) {
		this.beAttached = beAttached;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public void setStatic(boolean aStatic) {
		isStatic = aStatic;
	}
}
