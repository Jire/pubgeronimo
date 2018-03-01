package org.jire.pubgeronimo.protocol.channels;

import org.jire.pubgeronimo.Bunch;
import org.jire.pubgeronimo.NetGUIDCache;
import sun.plugin.dom.exception.InvalidStateException;

import static org.jire.pubgeronimo.protocol.Constants.RELIABLE_BUFFER;

public abstract class Channel {
	
	private final int channelIndex;
	private final ChannelType chType;
	
	private static int globalInReliable = -1;
	
	private boolean bDormant;
	private boolean CLosing;
	private int InReliable = globalInReliable;
	private int numInRec;
	private Bunch inQueueFirst, inQueueLast;
	private Bunch inPartialBunch;
	
	public Channel(int channelIndex, ChannelType chType) {
		this.channelIndex = channelIndex;
		this.chType = chType;
	}
	
	public int getChannelIndex() {
		return channelIndex;
	}
	
	public ChannelType getChannelType() {
		return chType;
	}
	
	public void receiveRawBunch(Bunch bunch) {
		if (bunch.bHasPackageMapExports)
			receiveNetGUIDBunch(bunch);
		if (bunch.bReliable && bunch.ChSequence <= InReliable) return;
		if (globalInReliable < 0) {
			globalInReliable = bunch.ChSequence - 1;
			InReliable = globalInReliable;
		}
		
		if (bunch.bReliable && bunch.ChSequence != InReliable + 1) {
			if (inQueueFirst == null) {
				inQueueFirst = bunch;
				inQueueLast = bunch;
			} else if (bunch.ChSequence < inQueueFirst.ChSequence) {
				bunch.setNext(inQueueFirst);
				inQueueFirst = bunch;
			} else if (bunch.ChSequence > inQueueLast.ChSequence) {
				inQueueLast.setNext(bunch);
				inQueueLast = bunch;
			} else {
				Bunch pre = this.inQueueFirst;
				while (bunch.ChSequence > pre.getNext().ChSequence)
					pre = pre.getNext();
				bunch.setNext(pre.getNext());
				pre.setNext(bunch);
			}
			
			numInRec++;
			if (numInRec >= RELIABLE_BUFFER)
				throw new InvalidStateException("Too many reliable messages queued up");
		} else {
		
		}
	}
	
	public void receiveNetGUIDBunch(Bunch bunch) {
		final boolean bHasRepLayoutExport = bunch.readBit();
		NetGUIDCache.INSTANCE.setExportingNetGUIDBunch(true);
		final int NumGUIDsInBunch = bunch.readInt32();
		if (NumGUIDsInBunch > 2048) NetGUIDCache.INSTANCE.setExportingNetGUIDBunch(false);
		
		for (int i = 0; i < NumGUIDsInBunch; i++) bunch.readObject();
		NetGUIDCache.INSTANCE.setExportingNetGUIDBunch(false);
	}
	
	public boolean receivedNextBunch(Bunch bunch) {
		if (bunch.bReliable) InReliable = bunch.ChSequence;
		
		Bunch HandleBunch = bunch;
		if (bunch.bPartial) {
			HandleBunch = null;
			if (bunch.bPartialInitial) {
				Bunch inPartialBunch = this.inPartialBunch;
				if (inPartialBunch != null) {
					if (!inPartialBunch.bPartialFinal) {
						if (inPartialBunch.bReliable)
							return false;
					}
					this.inPartialBunch = null;
				}
				
				this.inPartialBunch = bunch;
				if (!bunch.bHasPackageMapExports && bunch.bitsLeft() > 0 && bunch.bitsLeft() % 8 != 0)
					throw new IllegalStateException();
			} else {
				Bunch inPartialBunch = this.inPartialBunch;
			}
		}
	}
	
	public abstract void decode(Bunch bunch);
	
}
