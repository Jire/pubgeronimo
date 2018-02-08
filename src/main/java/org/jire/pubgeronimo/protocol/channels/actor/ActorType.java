package org.jire.pubgeronimo.protocol.channels.actor;

public enum ActorType {
	
	OTHER,
	GAME_STATE("Default__TSLGameState"),
	DROPPED_ITEM_GROUP("DroppedItemGroup"),
	GRENADE("SmokeBomb", "Molotov", "Grenade", "FlashBang", "BigBomb"),
	TWO_SEAT_BOAT("AquaRail"),
	SIX_SEAT_BOAT("boat"),
	TWO_SEAT_CAR("bike", "buggy", "SideCar"),
	THREE_SEAT_CAR,
	FOUR_SEAT_CAR("dacia", "uaz", "pickup"),
	SIX_SEAT_CAR("van", "bus"),
	PLANE("Aircraft"),
	PLAYER("Default__Player"),
	PARACHUTE("Parachute"),
	AIR_DROP("Carapackage"),
	PLAYER_STATE("Default__TslPlayerState"),
	TEAM("Default__Team"),
	DEATH_DROP_ITEM_PACKAGE("DeathDropItemPackage");
	
	private final String[] contains;
	
	ActorType(String... contains) {
		this.contains = contains;
	}
	
	public static final ActorType[] cachedValues = values();
	
	public static ActorType typeForString(String string) {
		for (ActorType type : cachedValues) {
			if (type.contains != null && type.contains.length > 0) {
				for (String contain : type.contains) {
					if (string.toLowerCase().contains(contain.toLowerCase())) {
						return type;
					}
				}
			}
		}
		return OTHER;
	}
	
}
