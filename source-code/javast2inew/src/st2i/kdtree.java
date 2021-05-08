package st2i;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class wpoimicode implements Comparator<key_value> {
	int poscheck;

	public wpoimicode(int key_type) {
		// TODO Auto-generated constructor stub
		this.poscheck = key_type;

	}

	@Override
	public int compare(key_value a, key_value b) {
		// TODO Auto-generated method stub

		return a.array[poscheck] - b.array[poscheck];
	}
}

public class kdtree {

	public static long readpos = 0;

	// static List<tweet> e;
	static key_value[] e;
	static int freenode = 1;
	static int kblock = 0;

	static knode[] narray;
	static int bytepos = 0;
	static long kblindex = 0;
	static int knodeNum = 0;
	static int rowS=5000000; 

	static FileOutputStream oout1;
	static FileOutputStream oout2;
	static Read_CSV read_csv;

	public static key_value[] readBinaryFile(String output, String output1)
			throws FileNotFoundException, IOException, ClassNotFoundException, ParseException {

		List<key_value> num1s = new ArrayList<key_value>();

		key_value te = new key_value();

		int i = 0;

		try {

			num1s = read_csv.processCSVFile(output, output1);

		}

		catch (EOFException exc) {

		}

		key_value[] array1 = new key_value[num1s.size()];

		array1 = (key_value[]) num1s.toArray(array1);
		num1s.clear();

		return array1;

	}

	public static void buildtree(int depth,  int index, int statindex, int endindex, int checker)
			throws IOException {
		// System.out.println(depth);
		knode temp = new knode();

		if (endindex - statindex <= kblock && endindex - statindex > 0) {
			int readlength = 0;

			knodeNum += 1;
			ByteBuffer buffer = null;

			for (int i = statindex; i < endindex; i++) {
				key_value t = e[i];

				buffer = key_value.wpoi2bytearray(t);

				readlength = readlength + t.array.length * 4;

				oout1.write(buffer.array());

			}

			temp.medvalue = readpos;

			temp.childnodeindex = (long) (readlength) << 32 | (-2) & 0xFFFFFFFFL;
			readpos += readlength;
			narray[index] = temp;
			if(endindex>=rowS) {
				rowS+=5000000;
			System.out.println(endindex+" size sorting done");
			
			}

			return;

		} else if (endindex - statindex == 0) {

			return;
		}

		int medianindex = (endindex - statindex) / 2 - 1;
		int check = depth % read_csv.check_key.length;

		int median = 0;

		Arrays.sort(e, statindex, endindex, new wpoimicode(check));

		median = e[statindex + medianindex].array[check];
		// System.out.println(median);
		temp.medvalue = (long) median << 32 | (-1) & 0xFFFFFFFFL;

		temp.childnodeindex = (long) freenode << 32 | (-1) & 0xFFFFFFFFL;
		// System.out.println("index freenode "+freenode);
		long ina = temp.childnodeindex;

		narray[index] = temp;
		knodeNum += 1;
		freenode += 2;

		buildtree(depth + 1,  (int) (ina >> 32), statindex, statindex + medianindex + 1, 1);
		statindex = statindex + medianindex + 1;
		buildtree(depth + 1,  (int) (ina >> 32) + 1, statindex, endindex, 2);

	}

	public static void running(String originaldata, int k, int[] check_key, int[] target_key)
			throws ParseException, IOException {

		String[] ip = originaldata.split("\\.");
		String knodef = ip[0] + "knode.dat";
		String stringLoc = ip[0] + "recordL.dat";
		String kbloc = ip[0] + "kblock.dat";
		String strdata = ip[0] + "strdata.dat";
		kblock = k;
		read_csv = new Read_CSV(check_key, target_key);
		try {
			e = readBinaryFile(originaldata, stringLoc);
			System.out.println("read finished e");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		oout1 = new FileOutputStream(kbloc);
		narray = new knode[2 * e.length];

		buildtree(0,  0, 0, e.length, 0);

System.out.println("kblock file size  "+ oout1.getChannel().size()+" bytes");
		oout1.close();
		oout1.flush();

		FileOutputStream out = new FileOutputStream(knodef);
		ByteBuffer dbuf = ByteBuffer.allocate(4);
	//	dbuf.putInt(k);
	//	out.write(dbuf.array());
		int c = 0;

		 dbuf = ByteBuffer.allocate(4);
		dbuf.putInt(check_key.length);
		out.write(dbuf.array());
		while (c < check_key.length) {
			dbuf = ByteBuffer.allocate(4);
			dbuf.putInt(check_key[c]);
			out.write(dbuf.array());
			c++;
		}

		c = 0;
		while (c < check_key.length) {
			dbuf = ByteBuffer.allocate(4);
			dbuf.putInt(target_key[c]);
			out.write(dbuf.array());
			c++;
		}

		c = 0;
		while (c < narray.length) {
			knode t = narray[c];
			if (t != null) {

				out.write(knode.knode2byte(t));

				c++;

			} else
				break;

		}
		System.out.println(c + " total node");
System.out.println("kdtree file size  "+ out.getChannel().size()+" bytes");
		out.close();

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, ParseException {

	String input = args[0];// "testdata1.csv";
//String input="testdata65new.csv";
	String nknode = args[1];// "testdata1.csv";
		//String nknode="512";
		String input2 = args[2];// "testdata1.csv";
		// String input2="kdtree_parameter_puda.txt";
		Path file = Paths.get(input2);

		InputStream in = Files.newInputStream(file);

		int[] check_key;
		int[] target_key;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

			String line = null;
			int i = 0;

			// StringBuilder response = new StringBuilder();

			line = br.readLine();
			line = br.readLine();
			String[] t = line.split(",");
			check_key = new int[t.length];
			while (i < t.length) {
				check_key[i] = Integer.parseInt(t[i]);

				i++;
			}
			line = br.readLine();
			line = br.readLine();
			t = line.split(",");
			i = 0;
			target_key = new int[t.length];
			while (i < t.length) {
				target_key[i] = Integer.parseInt(t[i]);
				i++;
			}

		}

		int n_knode = Integer.parseInt(nknode);
		running(input, n_knode, check_key, target_key);

	}

}