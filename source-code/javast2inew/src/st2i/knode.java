package st2i;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;

public class knode implements Serializable {

	public long childnodeindex = Long.MIN_VALUE;
	public long medvalue = Long.MIN_VALUE;

	knode() {

	}

	public static byte[] knode2byte(knode a) {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		ByteBuffer dbuf = ByteBuffer.allocate(8);
		// ByteBuffer wrapped; // big-endian by default
		// int num;
		// short num = wrapped.getShort();
		// ByteBuffer wrapped;
		dbuf.putLong(a.childnodeindex);
		byte[] bytes = dbuf.array();
		buffer.put(bytes);
		dbuf = ByteBuffer.allocate(8);
		dbuf.putLong(a.medvalue);
		byte[] bytes1 = dbuf.array();
		buffer.put(bytes1);
		// wrapped = ByteBuffer.wrap(bytes);
		// System.out.println(wrapped.getInt()+" ");

		return buffer.array();
	}

	public knode byte2knode(byte[] b) {

		knode a = new knode();
		ByteBuffer wrapped;
		byte[] temp = Arrays.copyOfRange(b, 0, 8);
		wrapped = ByteBuffer.wrap(temp); // big-endian by default
		a.childnodeindex = wrapped.getLong();

		// uint32_t dropoff_time;
		temp = new byte[8];
		temp = Arrays.copyOfRange(b, 8, 16);
		wrapped = ByteBuffer.wrap(temp); // big-endian by default
		a.medvalue = wrapped.getLong();
		// System.out.println("byte2knode "+a.childnodeindex+" "+a.medvalue);

		return a;

	}
	public knode(long v1,long v2) {

		//knode a = new knode();
	
		this.childnodeindex = v1;

		// uint32_t dropoff_time;
		
		this.medvalue = v2;
		// System.out.println("byte2knode "+a.childnodeindex+" "+a.medvalue);

	//	return a;

	}

	public static void main(String[] args) throws ParseException, IOException {
		knode a = new knode();
		a.childnodeindex = 100;
		a.medvalue = 121334312312L;
		a.knode2byte(a);
		a.byte2knode(a.knode2byte(a));
	}
}
