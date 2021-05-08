package st2i;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class key_value implements Serializable {

	int[] array;

	key_value(int[] array) {
		this.array = array;
	}

	key_value() {

	}

	public void setArray(int[] array) {
		this.array = array;
	}

	public static ByteBuffer wpoi2bytearray(key_value a) {

		ByteBuffer buffer = ByteBuffer.allocate(a.array.length * 4);

		int i = 0;
		while (i < a.array.length) {
			ByteBuffer dbuf = ByteBuffer.allocate(4);
			dbuf.putInt(a.array[i]);
			byte[] bytes = dbuf.array();
			buffer.put(bytes);
			i++;
		}

		return buffer;
	}

}
