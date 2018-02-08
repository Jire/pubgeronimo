package org.jire.pubgeronimo;

import java.nio.charset.Charset;

public final class PUBGBuffer {
	
	private static final byte zeroByte = 0;
	private static final float shortRotationScale = 360F / 65536F;
	private static final float byteRotationScale = 360F / 256F;
	
	private static final byte[] GShift = new byte[8];
	
	static {
		for (int i = 0; i < GShift.length; i++) {
			GShift[i] = (byte) (1 << i);
		}
	}
	
	/* Constants begin */
	public static final int NAME_SIZE = 1024;
	public static final int MAX_NETWORKED_HARDCODED_NAME = 410;
	public static final int MAX_PACKET_ID = 16384;
	public static final int MAX_CHANNELS = 10240;
	public static final int MAX_CH_SEQUENCE = 1024;
	public static final int MAX_PACKET_SIZE = 1228;
	
	public static final int CHTYPE_NONE = 0;
	public static final int CHTYPE_MAX = 8;
	/* Constants end */
	
	private final byte[] raw;
	
	private int posBits;
	private int localTotalBits;
	private int totalBits;
	
	private PUBGBuffer nextBuffer = null;
	private PUBGBuffer last = this;
	private PUBGBuffer cur = this;
	
	public PUBGBuffer(byte[] raw, int posBits, int localTotalBits, int totalBits) {
		this.raw = raw;
		this.posBits = posBits;
		this.localTotalBits = localTotalBits;
		this.totalBits = totalBits;
	}
	
	public PUBGBuffer(byte[] raw, int localTotalBits, int totalBits) {
		this(raw, 0, localTotalBits, totalBits);
	}
	
	public PUBGBuffer(byte[] raw, int localTotalBits) {
		this(raw, localTotalBits, localTotalBits);
	}
	
	public PUBGBuffer(byte[] raw) {
		this(raw, raw.length * 8);
	}
	
	private PUBGBuffer shallowCopy(int maxTotalBits) {
		return new PUBGBuffer(raw, posBits, Math.min(maxTotalBits, localTotalBits));
	}
	
	private PUBGBuffer shallowCopy() {
		return shallowCopy(localTotalBits);
	}
	
	public void append(PUBGBuffer buffer) {
		last.nextBuffer = buffer;
		last = buffer.last;
		totalBits += buffer.totalBits;
	}
	
	public boolean notEnd() {
		return bitsLeft() > 0;
	}
	
	public boolean atEnd() {
		return bitsLeft() <= 0;
	}
	
	public int bitsLeft() {
		return totalBits;
	}
	
	public int numBytes() {
		return (bitsLeft() + 7) << 3;
	}
	
	private boolean readLocalBit() {
		int b = raw[posBits >>> 3] & GShift[posBits & 0x7];
		posBits++;
		localTotalBits--;
		return b != zeroByte;
	}
	
	public boolean readBit() {
		if (cur.localTotalBits > 0) {
			totalBits--;
			return cur.readLocalBit();
		} else while (cur.nextBuffer != null) {
			cur = cur.nextBuffer;
			if (cur.localTotalBits > 0) {
				totalBits--;
				return cur.readLocalBit();
			}
		}
		
		throw new IndexOutOfBoundsException();
	}
	
	public int readByte() {
		int value = 0;
		for (int i = 0; i < 8; i++)
			if (readBit()) value |= GShift[i & 0x7];
		return value & 0xFF;
	}
	
	public byte[] readBytes(int sizeBytes) {
		return readBits(sizeBytes * 8);
	}
	
	public byte[] readBytes() {
		return readBytes(1);
	}
	
	public byte[] readBits(int sizeBits) {
		final byte[] value = new byte[(sizeBits + 7) >>> 3];
		for (int i = 0; i < sizeBits; i++) {
			if (readBit()) {
				int b = i >>> 3;
				value[b] = (byte) (value[b] | GShift[i & 0x7]);
			}
		}
		return value;
	}
	
	public byte[] readBits() {
		return readBits(8);
	}
	
	public int readInt(int maxValue) {
		int mask = 1;
		int value = 0;
		while (value + mask < maxValue && mask != 0) {
			if (readBit()) value |= mask;
			mask <<= 1;
		}
		return value;
	}
	
	public int readInt() {
		return readInt(MAX_PACKET_ID);
	}
	
	public int readIntPacked() {
		int value = 0;
		int count = 0;
		int more = 1;
		while (more != 0) {
			int nextByte = readByte();
			more = nextByte & 1;
			nextByte >>>= 1;
			value += nextByte << (7 * count++);
		}
		return value;
	}
	
	public int readInt8() {
		return (byte) readByte();
	}
	
	public int readUInt8() {
		return readByte();
	}
	
	public int readInt16() {
		return (short) readUInt16();
	}
	
	public int readUInt16() {
		int value = readByte();
		value |= (readByte() << 8);
		return value;
	}
	
	public int readInt32() {
		return (int) readUInt32();
	}
	
	public long readInt64() {
		long value = readByte();
		value |= ((long) readByte()) << 8;
		value |= ((long) readByte()) << 16;
		value |= ((long) readByte()) << 24;
		value |= ((long) readByte()) << 32;
		value |= ((long) readByte()) << 40;
		value |= ((long) readByte()) << 48;
		value |= ((long) readByte()) << 56;
		return value;
	}
	
	public int readInt24() {
		int value = readByte();
		value |= (readByte() << 8);
		value |= (readByte() << 16);
		return value;
	}
	
	public long readUInt32() {
		long value = readByte();
		value |= (readByte() << 8);
		value |= (readByte() << 16);
		value |= (readByte() << 24);
		return value;
	}
	
	public float readFloat() {
		int value = readByte();
		value |= (readByte() << 8);
		value |= (readByte() << 16);
		value |= (readByte() << 24);
		return Float.intBitsToFloat(value);
	}
	
	public String readString() {
		int saveNum = readInt32();
		boolean loadUCS2Char = saveNum < 0;
		if (loadUCS2Char)
			saveNum = -saveNum;
		if (saveNum > NAME_SIZE)
			throw new ArrayIndexOutOfBoundsException();
		if (saveNum == 0) return "";
		
		if (loadUCS2Char)
			return new String(readBytes(saveNum * 2), Charset.forName("UTF-8"));
		
		final byte[] bytes = readBytes(saveNum);
		return new String(bytes, 0, bytes.length - 1, Charset.forName("UTF-8"));
	}
	
	public String readName() {
		final boolean hardcoded = readBit();
		if (hardcoded) {
			final int nameIndex = readInt(MAX_NETWORKED_HARDCODED_NAME + 1);
			return "";
		}
		
		final String inString = readString();
		final int inNumber = readInt32();
		return inString;
	}
	
	public void skipNetFieldExport() {
		int flags = readUInt8();
		if (flags == 1) {
			readIntPacked();
			readUInt32();
			readString();
			readString();
		}
	}
	
	public NetworkGUID readNetworkGUID() {
		return new NetworkGUID(readIntPacked());
	}
	
	public ObjectPtr readObject() {
		final NetworkGUID netGUID = readNetworkGUID();
		if (!netGUID.isValid()) return new ObjectPtr(netGUID, null);
		
		NetGUIDObject obj = null;
		
		if (netGUID.isDefault()) {
			final ExportFlags exportFlags = new ExportFlags(readUInt8());
			if (exportFlags.bHasPath()) {
				final ObjectPtr objectPtr = readObject();
				final NetworkGUID outerGUID = objectPtr.getNetworkGUID();
				final NetGUIDObject outerObj = objectPtr.getNetGUIDObject();
				
				final String pathName = readString();
				final int networkChecksum = exportFlags.bHasNetworkChecksum() ? (int) readUInt32() : 0;
				final boolean bIsPackage = netGUID.isStatic() && !outerGUID.isValid();
				
				//if (obj != null)
				if (netGUID.isDefault()) { // assign GUID
					return new ObjectPtr(netGUID, null);
				}
				
				final boolean bIgnoreWhenMissing = exportFlags.bNoLoad();
				
				obj = new NetGUIDObject(pathName, outerGUID, networkChecksum, exportFlags.bNoLoad(), bIgnoreWhenMissing);
			}
		}
		return new ObjectPtr(netGUID, obj);
	}
	
	public void skipBits(int bits) {
		int decrease = Math.min(cur.localTotalBits, bits);
		cur.localTotalBits -= decrease;
		cur.posBits += decrease;
		totalBits -= decrease;
		bits -= decrease;
		
		while (bits > 0 && cur.nextBuffer != null) {
			cur = cur.nextBuffer;
			decrease = Math.min(cur.localTotalBits, bits);
			cur.localTotalBits -= decrease;
			cur.posBits += decrease;
			totalBits -= decrease;
			bits -= decrease;
		}
	}
	
	public Vector3 readRotationShort() {
		return new Vector3((readBit() ? readUInt16() : 0) * shortRotationScale,
				(readBit() ? readUInt16() : 0) * shortRotationScale,
				(readBit() ? readUInt16() : 0) * shortRotationScale);
	}
	
	public Vector3 readRotation() {
		return new Vector3((readBit() ? readUInt8() : 0) * shortRotationScale,
				(readBit() ? readUInt8() : 0) * shortRotationScale,
				(readBit() ? readUInt8() : 0) * shortRotationScale);
	}
	
	public Vector3 readVector(int scaleFactor, int maxBitsPerComponent) {
		final int bits = readInt(maxBitsPerComponent);
		final int bias = 1 << (bits + 1);
		final int max = 1 << (bits + 2);
		return new Vector3(
				(readInt(max) - bias) / (float) scaleFactor,
				(readInt(max) - bias) / (float) scaleFactor,
				(readInt(max) - bias) / (float) scaleFactor
		);
	}
	
	public Vector3 readVector() {
		return readVector(10, 24);
	}
	
}
