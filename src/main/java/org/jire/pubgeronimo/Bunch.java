package org.jire.pubgeronimo;

public final class Bunch extends PUBGBuffer {
	
	public final int BunchDataBits, PacketID, ChIndex, ChType, ChSequence;
	public final boolean bOpen, bClose, bDormant, bIsReplicationPaused;
	public final boolean bReliable, bPartial, bPartialInitial, bPartialFinal;
	public final boolean bHasPackageMapExports, bHasMustBeMappedGUIDs;
	
	public Bunch(PUBGBuffer buffer, int bunchDataBits, int packetID, int chIndex, int chType, int chSequence,
	             boolean bOpen, boolean bClose, boolean bDormant, boolean bIsReplicationPaused,
	             boolean bReliable, boolean bPartial, boolean bPartialInitial,
	             boolean bPartialFinal, boolean bHasPackageMapExports, boolean bHasMustBeMappedGUIDs) {
		super(buffer);
		BunchDataBits = bunchDataBits;
		PacketID = packetID;
		ChIndex = chIndex;
		ChType = chType;
		ChSequence = chSequence;
		this.bOpen = bOpen;
		this.bClose = bClose;
		this.bDormant = bDormant;
		this.bIsReplicationPaused = bIsReplicationPaused;
		this.bReliable = bReliable;
		this.bPartial = bPartial;
		this.bPartialInitial = bPartialInitial;
		this.bPartialFinal = bPartialFinal;
		this.bHasPackageMapExports = bHasPackageMapExports;
		this.bHasMustBeMappedGUIDs = bHasMustBeMappedGUIDs;
	}
	
	@Override
	public PUBGBuffer deepCopy(int copyBits) {
		return new Bunch(
				super.deepCopy(copyBits),
				BunchDataBits,
				PacketID,
				ChIndex,
				ChType,
				ChSequence,
				bOpen,
				bClose,
				bDormant,
				bIsReplicationPaused,
				bReliable,
				bPartial,
				bPartialInitial,
				bPartialFinal,
				bHasPackageMapExports,
				bHasMustBeMappedGUIDs);
	}
	
}
