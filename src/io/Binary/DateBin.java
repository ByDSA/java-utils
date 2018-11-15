package io.Binary;

import java.nio.ByteBuffer;
import java.util.Date;

public class DateBin extends TypeBin<Long> {
	public DateBin(Long v) {
		super( v );
	}
	
	public DateBin(Date v) {
		super( v.getTime() );
	}

	@Override
	public int size() {
		return Long.BYTES;
	}

	@Override
	public void addBuff(ByteBuffer buff) {
		buff.putLong( get() );
	}
	
	public Date getDate() {
		return new Date( get() );
	}
	
	@Override
	public void read(ByteBuffer buff) {
		set( buff.getLong() );
	}

}
