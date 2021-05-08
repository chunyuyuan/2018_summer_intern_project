package st2i;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Comparator;
import java.util.Date;

import java.util.List;
import java.util.TimeZone;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Query {
	static List<knode> knodearray;
	
	static byte[]positionbyte;
	static int searchresult = 0;
	static ArrayList<Integer> result;
	static ArrayList<key_value> aa;
	static int l;
	static int r;
	static int check_key[];
	static int check_keyl;
	static int target_key[];
	static CSVRecord csvRecord_header;
	static int[][] query_range;
	static RandomAccessFile fileb;// = new RandomAccessFile(kblockf, "r");
	static byte[] read;

	static BufferedWriter bw = null;

	

	

	void setRange(int i, int id0, int id1) {
		query_range[i][0] = id0;
		query_range[i][1] = id1;
	}

	public static int float2Int(float i) {
		return (int) (i * 10000);
	}

	/*
	 * public static float int2Float(float i) { float te = (float) (i / 1000000);
	 * return te; }
	 */
	// use byte array to convert
	public static float int2Float(int i) {

		return ((float) i / 10000);
	}

	public static void searchTree(int index, int depth, int[][] range) {

		knode a = knodearray.get(index);

		int flag1 = (int) (a.childnodeindex >> 32);
		int flag2 = (int) a.childnodeindex;

		if (flag2 == -2) {

			result.add(index);
			return;
		}

		int rangeIndex = (depth) % check_keyl;
		long tem = a.medvalue;
		int median = (int) (tem >> 32);

		int nextIndex = flag1;

		if (range[rangeIndex][0] <= median && range[rangeIndex][1] >= median) {
			searchTree(nextIndex, depth + 1, range);
			nextIndex = flag1 + 1;

			searchTree(nextIndex, depth + 1, range);

		} else {
			if (range[rangeIndex][1] < median)
				searchTree(nextIndex, depth + 1, range);
			if (range[rangeIndex][0] > median) {
				nextIndex = flag1 + 1;

				searchTree(nextIndex, depth + 1, range);
			}
		}

	}

	public static void searchTree_r(int index, int depth) throws IOException {

		knode a = knodearray.get(index);

		int flag1 = (int) (a.childnodeindex >> 32);
		int flag2 = (int) a.childnodeindex;

		if (flag2 == -2) {

			// byte[] read = new byte[flag1];
			// System.out.println(flag1);
			try {
				fileb.seek(a.medvalue);
				// System.out.println(posItem * 224);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileb.read(read, 0, flag1);

			// key_value twe = new key_value();
			int byteflag = 0;
			
			while (byteflag < flag1) {

				int i = 0;
				int[] ca = new int[check_keyl + 1];
				while (i < check_keyl + 1) {

					ca[i] = read[byteflag] << 24 | (read[byteflag + 1] & 0xFF) << 16 | (read[byteflag + 2] & 0xFF) << 8
							| (read[byteflag + 3] & 0xFF);
					i++;
					byteflag += 4;
				}
				i = 0;
				boolean checker = true;
				while (i < query_range.length) {

					if (query_range[i][0] <= ca[i] && ca[i] <= query_range[i][1]) {

					} else {
						checker = false;
						break;
					}
					i++;
				}

				if (checker) {

					key_value twe = new key_value(ca);

					aa.add(twe);

				}

			}

			// file.close();
			return;
		}

		int rangeIndex = (depth) % check_keyl;
		long tem = a.medvalue;
		int median = (int) (tem >> 32);

		int nextIndex = flag1;

		if (query_range[rangeIndex][0] <= median && query_range[rangeIndex][1] >= median) {
			searchTree_r(nextIndex, depth + 1);
			nextIndex = flag1 + 1;

			searchTree_r(nextIndex, depth + 1);

		} else {
			if (query_range[rangeIndex][1] < median)
				searchTree_r(nextIndex, depth + 1);
			if (query_range[rangeIndex][0] > median) {
				nextIndex = flag1 + 1;

				searchTree_r(nextIndex, depth + 1);
			}
		}

	}

	public static void detailFilter(int[][] range) throws IOException {

		// RandomAccessFile file = new RandomAccessFile(kblockf, "r");
		// int x=0;

		System.out.println("leaf node " + result.size());
		for (int j : result) {
			knode a = knodearray.get(j);
			int flag1 = (int) (a.childnodeindex >> 32);

			// byte[] read = new byte[flag1];
			try {
				fileb.seek(a.medvalue);
				// System.out.println(posItem * 224);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileb.read(read, 0, flag1);

			key_value twe = new key_value();
			int byteflag = 0;
			
			while (byteflag < flag1) {
				byte[] taxibyte = Arrays.copyOfRange(read, byteflag, byteflag + (check_keyl + 1) * 4);

				twe = new key_value();
				int[] ca = new int[check_keyl + 1];
			//	int[] ca = new int[check_keyl + 1];
				int i=0;
				while (i < check_keyl + 1) {

					ca[i] = read[byteflag] << 24 | (read[byteflag + 1] & 0xFF) << 16 | (read[byteflag + 2] & 0xFF) << 8
							| (read[byteflag + 3] & 0xFF);
					i++;
					byteflag += 4;
				}
				/*int i = 0;
				while (i < check_keyl + 1) {

					ca[i] = ByteBuffer.wrap(Arrays.copyOfRange(taxibyte, i * 4, (i + 1) * 4)).getInt();
					i++;
				}*/
				i = 0;
				boolean checker = true;
				while (i < range.length) {
					// if(i==2)
					// System.out.println((range[i][0] +" " +ca[0]+" "+ca[1]+" "+ca[2]+" "+i+" "+
					// range[i][1]));
					if (range[i][0] <= ca[i] && ca[i] <= range[i][1]) {

					} else {
						checker = false;
						break;
					}
					i++;
				}

				if (checker) {

					twe = new key_value(ca);

					aa.add(twe);

				}

			//	byteflag = byteflag + (check_keyl + 1) * 4;

			}

		}

	}

	public static void detailFilterchecker(int[][] range) throws IOException {

		// RandomAccessFile file = new RandomAccessFile(kblockf, "r");
		searchresult = 0;
		int ck;
		knode a;
		int flag1;
	
		for (int j = 0; j < result.size(); j++) {
			 ck = result.get(j);

			 a = knodearray.get(ck);
		 flag1 = (int) (a.childnodeindex >> 32);
			// int flag2 = (int) a.childnodeindex;

			try {
				fileb.seek(a.medvalue);
				// System.out.println(posItem * 224);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileb.read(read, 0, flag1);
		
			int byteflag = 0;
			
		
		
			while (byteflag < flag1) {
			//	byte[] taxibyte = Arrays.copyOfRange(read, byteflag, byteflag + (check_keyl + 1) * 4);
				int[] temp = new int[check_keyl];
				
				int i = 0;
				while (i < check_keyl ) {

					temp[i] = read[byteflag] << 24 | (read[byteflag + 1] & 0xFF) << 16 | (read[byteflag + 2] & 0xFF) << 8
							| (read[byteflag + 3] & 0xFF);
					i++;
					byteflag += 4;
				}
				byteflag += 4;
				i = 0;
				boolean checker = true;
				while (i < temp.length) {
					if (range[i][0] <= temp[i] && temp[i] <= range[i][1]) {

					} else {

						checker = false;
						break;
					}
					i++;
				}

				if (checker) {

					searchresult += 1;

				}

				//byteflag = byteflag + (check_keyl + 1) * 4;

			}

		}

	}

	public static void nearestK(int k, int[][] range, int lo_i, int la_i) throws IOException {

		if (k <= 0) {
			return;
		}

		searchresult = 0;
		// Query q = new Query();
		// r = 80000000;
		int rangeLoc = r;

		
		boolean checker = false;
		int[][] range1;
		while (r <= 80000000) {

			while (rangeLoc <= r && rangeLoc > l) {

				result = new ArrayList<Integer>();
				// System.out.println(searchresult+"size "+rangeLoc+" "+l+" "+r);
				range1 = new int[range.length][2];
				int j = 0;
				while (j < range.length) {
					if (j == lo_i) {
						range1[j][0] = range[j][0] - rangeLoc;
						range1[j][1] = range[j][1] + rangeLoc;
					} else if (j == la_i) {
						range1[j][0] = range[j][0] - rangeLoc;
						range1[j][1] = range[j][1] + rangeLoc;
					} else {
						range1[j] = range[j];
					}

					j++;
				}

				searchTree(0, 0, range1);

				searchresult = 0;

				detailFilterchecker(range1);

				if (searchresult - k <= 10000 && searchresult >= k) {

					detailFilter(range1);

					checker = true;

					break;
				} else if (searchresult < k) {

					// System.out.println(rangeLoc+" "+r+" "+l);
					if (rangeLoc == 80000000) {
						detailFilter(range1);
						break;
					}

					int inc = (r - l) / 2;
					l = rangeLoc;

					rangeLoc = l + inc;

				} else if (searchresult - k > 10000) {

					int inc = (r - l) / 2;

					r = rangeLoc;

					rangeLoc = l + inc;

				}

			}
			if (checker == true)
				break;
			r = r * 2;
			l = 0;
			rangeLoc = r;

		}

		aa.sort(new Comparator<key_value>() {

			@Override
			public int compare(key_value arg0, key_value arg1) {

				return new Double(Math.pow(arg0.array[lo_i] - (range[lo_i][0] + range[lo_i][1]) / 2, 2)
						+ Math.pow(arg0.array[la_i] - (range[la_i][0] + range[la_i][1]) / 2, 2)).compareTo(
								new Double(Math.pow(arg1.array[lo_i] - (range[lo_i][0] + range[lo_i][1]) / 2, 2)
										+ Math.pow(arg1.array[la_i] - (range[la_i][0] + range[la_i][1]) / 2, 2)));

			}

		});

	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		return os.startsWith("Windows");
	}

	public static void outprintresult(int k, int range[][], int lo_i, int la_i, int[] other_fields, String originaldata)
			throws IOException {
		String[] ip = originaldata.split("\\.");
		String strdata = originaldata;
	/*	if (isWindows()) {

		} else {

			strdata = ip[0] + "strdata.dat";
		}*/

		RandomAccessFile raf = new RandomAccessFile(strdata, "rw");
		if (k >= aa.size())
			k = aa.size();
		long s1 = System.currentTimeMillis();

		bw.write("\n  long " + (range[lo_i][0] + range[lo_i][1]) / 2 + " lat "
				+ (range[la_i][0] + range[la_i][1]) / 2 + " " + k);
		bw.newLine();
	
		int c;
		byte[] te;
		CSVFormat csvFormat;
		CSVParser csvParser;
		CSVRecord csvRecord;
		StringBuilder content;
		int id;
		long pos,pos1;
		for (key_value tr : aa) {
			if (--k < 0)
				break;
			c = check_keyl;
		    id=(tr.array[c]-1)*8;
		    pos= (long)(
		            // (Below) convert to longs before shift because digits
		            //         are lost with ints beyond the 32-bit limit
		            (long)(0xff & positionbyte[id+0]) << 56  |
		            (long)(0xff & positionbyte[id+1]) << 48  |
		            (long)(0xff & positionbyte[id+2]) << 40  |
		            (long)(0xff & positionbyte[id+3]) << 32  |
		            (long)(0xff & positionbyte[id+4]) << 24  |
		            (long)(0xff & positionbyte[id+5]) << 16  |
		            (long)(0xff & positionbyte[id+6]) << 8   |
		            (long)(0xff & positionbyte[id+7]) << 0
		            );
		    id=(tr.array[c])*8;
		    pos1= (long)(
		            // (Below) convert to longs before shift because digits
		            //         are lost with ints beyond the 32-bit limit
		            (long)(0xff & positionbyte[id+0]) << 56  |
		            (long)(0xff & positionbyte[id+1]) << 48  |
		            (long)(0xff & positionbyte[id+2]) << 40  |
		            (long)(0xff & positionbyte[id+3]) << 32  |
		            (long)(0xff & positionbyte[id+4]) << 24  |
		            (long)(0xff & positionbyte[id+5]) << 16  |
		            (long)(0xff & positionbyte[id+6]) << 8   |
		            (long)(0xff & positionbyte[id+7]) << 0
		            );
			
			raf.seek(pos);

			// int c = check_key.length;

			te = new byte[(int) (pos1 - pos)];
			// System.out.println(te.length+" length "+positionarray.get(tr.array[3]+1));
			raf.readFully(te);
			 csvFormat = CSVFormat.DEFAULT.withQuote(null);

			 csvParser = CSVParser.parse(new String(te), csvFormat);

		     csvRecord = csvParser.getRecords().get(0);

			 content = new StringBuilder("");
			int i = 0;
			while (i < check_keyl) {
				switch (check_key[i]) {
				case 0:
					//content += "\n" + csvRecord_header.get(target_key[i]) + " " + tr.array[i];
					content.append( "\n" + csvRecord_header.get(target_key[i]) + " " + tr.array[i]);
					
					/*
					 * if(Integer.parseInt(k)== 10906040) { c++;
					 * //System.out.println("\n 10906040 c "+c+" "+i_key); }
					 */
					break;
				case 1:
					content.append( "\n" + csvRecord_header.get(target_key[i]) + " " + int2Float(tr.array[i]));
					// temp[i_key] = float2Int(Float.parseFloat(k));
					break;

				case -1:
					SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timeformat.setTimeZone(TimeZone.getTimeZone("EST"));
					// Date date=timeformat.parse(k);
					// long t=date.getTime()/1000;

					// temp[i_key]=(int)t;
					long t = (long) tr.array[i] * 1000;
					String date = timeformat.format(t);
					// temp[i_key]=(int)t;
					content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + date.toString());
					//content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + tr.array[i]);
					break;

				}

				i++;
			}
			content.append( "\ndistance "
					/*
					 * + new Double(Math.sqrt(Math.pow(int2Float(tr.array[lo_i]) -
					 * (int2Floatrange[lo_i][0] + range[lo_i][1]) / 2, 2) + Math.pow(tr.array[la_i]
					 * - (range[la_i][0] + range[la_i][1]) / 2, 2))) / 1000000;
					 */
					+ Math.hypot(
							int2Float(tr.array[lo_i])
									- (float) (int2Float(range[lo_i][0]) + int2Float(range[lo_i][1])) / 2,
							int2Float(tr.array[la_i])
									- (float) (int2Float(range[la_i][0]) + int2Float(range[la_i][1])) / 2));
			// + " \n";
			i = 0;
			// System.out.println(csvRecord.toString());
			while (i < other_fields.length) {
				content.append( "\n" + csvRecord_header.get(other_fields[i]) + " " + csvRecord.get(other_fields[i]));

				i++;
			}
			content .append( " \n");

			// System.out.println(content);
			bw.write(content.toString());
			bw.newLine();
		}
		bw.newLine();
		long s2 = System.currentTimeMillis();

		// System.out.println("temp "+temp.length);
		bw.write("running time " + (s2 - s1) + " ms");

	}

	public void processing_file(String originaldata) throws IOException {
		String[] ip = originaldata.split("\\.");
		String knode = ip[0] + "knode.dat";
		String stringLoc = ip[0] + "recordL.dat";
		// String kblock = ip[0]+"kblock.dat";

		knodearray = new ArrayList<>();
		
		Path fileLocation = Paths.get(knode);
		
		RandomAccessFile raf = null; // = new RandomAccessFile(originaldata, "rw");
		/*if (isWindows()) {*/
			raf = new RandomAccessFile(originaldata, "rw");
		/*} else {
			String strdata = ip[0] + "strdata.dat";
			raf = new RandomAccessFile(strdata, "rw");

		}*/
		long a3=System.currentTimeMillis();
		;
		byte[] data = Files.readAllBytes(fileLocation);
		int ii = 0;

		System.out.println("leafnode kdtree data length " + data.length);

		byte[] temp = Arrays.copyOfRange(data, ii, ii + 4);
		ii += 4;

		ByteBuffer wrapped;

		wrapped = ByteBuffer.wrap(temp); // big-endian by default
		int l = wrapped.getInt();
		check_key = new int[l];
		target_key = new int[l];
		int i = 0;
		while (i < l) {

			temp = Arrays.copyOfRange(data, ii, ii + 4);
			ii += 4;

			wrapped = ByteBuffer.wrap(temp); // big-endian by default
			check_key[i] = wrapped.getInt();
			// System.out.print("check_key " + check_key[i] + " ");
			i++;
		}
		check_keyl = check_key.length;
		read = new byte[check_keyl * 4 * 2048];
		// System.out.println(" ");
		i = 0;
		while (i < l) {

			temp = Arrays.copyOfRange(data, ii, ii + 4);
			ii += 4;

			wrapped = ByteBuffer.wrap(temp); // big-endian by default
			target_key[i] = wrapped.getInt();
			// System.out.print("target_key " + target_key[i] + " ");
			i++;
		}
		// System.out.println(" ");
		
		while (ii < data.length) {

			temp = Arrays.copyOfRange(data, ii, ii + 16);
			knode a1 = new knode();
			a1 = a1.byte2knode(temp);

			r++;

			knodearray.add(a1);
			ii += 16;

		}
		
		// System.out.println("512 leafnode kdtree data length " + data.length);
		Path fileLocation1 = Paths.get(stringLoc);
		positionbyte = Files.readAllBytes(fileLocation1);
		ii = 0;

		// int r=0;
		
		long a;
	/*	while (ii < data1.length) {

			//temp = Arrays.copyOfRange(data1, ii, ii + 8);

			//ByteBuffer bb = ByteBuffer.wrap(temp);

			 a = (long)(
		            // (Below) convert to longs before shift because digits
		            //         are lost with ints beyond the 32-bit limit
		            (long)(0xff & data1[ii+0]) << 56  |
		            (long)(0xff & data1[ii+1]) << 48  |
		            (long)(0xff & data1[ii+2]) << 40  |
		            (long)(0xff & data1[ii+3]) << 32  |
		            (long)(0xff & data1[ii+4]) << 24  |
		            (long)(0xff & data1[ii+5]) << 16  |
		            (long)(0xff & data1[ii+6]) << 8   |
		            (long)(0xff & data1[ii+7]) << 0
		            );
			// System.out.println("position "+a);
			 positionarray.add(a);
			ii += 8;

		}*/
		 a = (long)(
		            // (Below) convert to longs before shift because digits
		            //         are lost with ints beyond the 32-bit limit
		            (long)(0xff & positionbyte[0]) << 56  |
		            (long)(0xff & positionbyte[1]) << 48  |
		            (long)(0xff & positionbyte[2]) << 40  |
		            (long)(0xff & positionbyte[3]) << 32  |
		            (long)(0xff & positionbyte[4]) << 24  |
		            (long)(0xff & positionbyte[5]) << 16  |
		            (long)(0xff & positionbyte[6]) << 8   |
		            (long)(0xff & positionbyte[7]) << 0
		            );
		
		

		raf.seek(0);

		byte[] te = new byte[(int) (a)];
		// System.out.println(te.length+" length "+positionarray.get(tr.array[3]+1));
		raf.readFully(te);
		CSVFormat csvFormat = CSVFormat.DEFAULT.withQuote(null);

		CSVParser csvParser = CSVParser.parse(new String(te), csvFormat);
		// System.out.println(res+" "+tr.recordLegth);
		csvRecord_header = csvParser.getRecords().get(0);
		
	}

	public void search_topK(int k, int lo_i, int la_i, int[] other_field, String oginaldata) throws IOException {
		String[] ip = oginaldata.split("\\.");

		String kblock = ip[0] + "kblock.dat";
		String resultfile = ip[0] + "_result.txt";
		File file1 = new File(resultfile);

		System.out.println("Find K " + k + "\n");

		long resulttime = 0;
		long resulttime1 = 0;

		bw = new BufferedWriter(new FileWriter(file1));

		fileb = new RandomAccessFile(kblock, "r");

		// System.out.println(" test " + lalon[0] + " " + lalon[1]);
		long zt1 = System.currentTimeMillis();

		// q.setSearchArea32.7692f, -96.821986f, 32.7692f, -96.821986f);

		aa = new ArrayList<>();

		// gas station id

		nearestK(k, query_range, lo_i, la_i);

		long ctt2 = System.currentTimeMillis();

		outprintresult(k, query_range, lo_i, la_i, other_field, oginaldata);

		long ctt1 = System.currentTimeMillis();

		resulttime = (ctt1 - zt1);
		resulttime1 = (ctt2 - zt1);

		System.out.println("target size " + aa.size());

		bw.close();
		fileb.close();
		// System.out.println("////"+gasmicodeid[gasi]);
		double a = (double) resulttime;
		double b = (double) resulttime1;
		System.out.println("search done touch data (before IO ) average time " + b + " ms");
		System.out.println("search done (include IO ) average time " + a + " ms");

	}

	public static void outprintresult_r(List<key_value> a, int[] other_fields, String originaldata) throws IOException {
		String[] ip = originaldata.split("\\.");
		String strdata = originaldata;
		/*if (isWindows()) {

		} else {*/
	/*		strdata = ip[0] + "strdata.dat";
		}*/
		// String strdata = ip[0] + "strdata.dat";
		RandomAccessFile raf = new RandomAccessFile(strdata, "rw");

		long s1 = System.currentTimeMillis();

		CSVFormat csvFormat;
		CSVParser csvParser;
		CSVRecord csvRecord;
		StringBuilder content;
	
			int id;
			long pos,pos1;
		for (key_value tr : a) {

			int c = check_keyl;
			 id=(tr.array[c]-1)*8;
			    pos= (long)(
			            // (Below) convert to longs before shift because digits
			            //         are lost with ints beyond the 32-bit limit
			            (long)(0xff & positionbyte[id+0]) << 56  |
			            (long)(0xff & positionbyte[id+1]) << 48  |
			            (long)(0xff & positionbyte[id+2]) << 40  |
			            (long)(0xff & positionbyte[id+3]) << 32  |
			            (long)(0xff & positionbyte[id+4]) << 24  |
			            (long)(0xff & positionbyte[id+5]) << 16  |
			            (long)(0xff & positionbyte[id+6]) << 8   |
			            (long)(0xff & positionbyte[id+7]) << 0
			            );
			    id=(tr.array[c])*8;
			    pos1= (long)(
			            // (Below) convert to longs before shift because digits
			            //         are lost with ints beyond the 32-bit limit
			            (long)(0xff & positionbyte[id+0]) << 56  |
			            (long)(0xff & positionbyte[id+1]) << 48  |
			            (long)(0xff & positionbyte[id+2]) << 40  |
			            (long)(0xff & positionbyte[id+3]) << 32  |
			            (long)(0xff & positionbyte[id+4]) << 24  |
			            (long)(0xff & positionbyte[id+5]) << 16  |
			            (long)(0xff & positionbyte[id+6]) << 8   |
			            (long)(0xff & positionbyte[id+7]) << 0
			            ); 
			    id=(tr.array[c]+1)*8;
					    pos1= (long)(
					            // (Below) convert to longs before shift because digits
					            //         are lost with ints beyond the 32-bit limit
					            (long)(0xff & positionbyte[id+0]) << 56  |
					            (long)(0xff & positionbyte[id+1]) << 48  |
					            (long)(0xff & positionbyte[id+2]) << 40  |
					            (long)(0xff & positionbyte[id+3]) << 32  |
					            (long)(0xff & positionbyte[id+4]) << 24  |
					            (long)(0xff & positionbyte[id+5]) << 16  |
					            (long)(0xff & positionbyte[id+6]) << 8   |
					            (long)(0xff & positionbyte[id+7]) << 0
					            );
			raf.seek(pos);

			// int c = check_key.length;

			byte[] te = new byte[(int) (pos1 - pos)];
			// System.out.println(te.length+" length "+positionarray.get(tr.array[3]+1));
			raf.readFully(te);
			csvFormat = CSVFormat.DEFAULT.withQuote(null);

			csvParser = CSVParser.parse(new String(te), csvFormat);
			// System.out.println(res+" "+tr.recordLegth);

			// System.out.println(res+" "+tr.recordLegth);
			 csvRecord = csvParser.getRecords().get(0);

			content =new StringBuilder( "");
			int i = 0;
			while (i < check_keyl) {
				switch (check_key[i]) {
				case 0:
					content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + tr.array[i]);
					/*
					 * if(Integer.parseInt(k)== 10906040) { c++;
					 * //System.out.println("\n 10906040 c "+c+" "+i_key); }
					 */
					break;
				case 1:
					content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + int2Float(tr.array[i]));
					// temp[i_key] = float2Int(Float.parseFloat(k));
					break;

				case -1:
					SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timeformat.setTimeZone(TimeZone.getTimeZone("EST"));
					// Date date=timeformat.parse(k);
					long t = (long) tr.array[i] * 1000;
					String date = timeformat.format(t);
					// temp[i_key]=(int)t;
					content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + date.toString());
				//	content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + tr.array[i]);
				//	System.out.println(date.toString());
				//	break;

				}

				i++;
			}

			i = 0;
			// System.out.println(csvRecord.toString());
			while (i < other_fields.length) {
				if (other_fields[i] != -1)
					content .append( "\n" + csvRecord_header.get(other_fields[i]) + " " + csvRecord.get(other_fields[i]));

				i++;
			}
			content .append(" \n");

			// System.out.println(content);
			bw.write(content.toString());
			bw.newLine();
		}
		bw.newLine();
		long s2 = System.currentTimeMillis();

		// System.out.println("temp "+temp.length);
		bw.write("running time " + (s2 - s1) + " ms");

	}

	public void search_range(int[] other_field, String oginaldata) throws IOException {
		String[] ip = oginaldata.split("\\.");

		String kblock = ip[0] + "kblock.dat";
		String resultfile = ip[0] + "_result.txt";
		File file1 = new File(resultfile);

		long resulttime = 0;
		long resulttime1 = 0;

		bw = new BufferedWriter(new FileWriter(file1));

		// System.out.println(" test " + lalon[0] + " " + lalon[1]);
		long zt1 = System.currentTimeMillis();

		// q.setSearchArea(32.7692f, -96.821986f, 32.7692f, -96.821986f);

		aa = new ArrayList<>();

		// gas station id

		result = new ArrayList<Integer>();
		// System.out.println(searchresult+"size "+rangeLoc+" "+l+" "+r);
		fileb = new RandomAccessFile(kblock, "rw");
		// searchTree(0, 0, query_range, result);

		// searchresult = 0;

		// detailFilter(result, kblock,query_range, aa);
		searchTree_r(0, 0);

		searchresult = 0;

		// detailFilter(result, kblock, range, aa);
		// System.out.println(aa.get(0).lat);
		long ctt2 = System.currentTimeMillis();

		outprintresult_r(aa, other_field, oginaldata);

		long ctt1 = System.currentTimeMillis();

		resulttime = (ctt1 - zt1);
		resulttime1 = (ctt2 - zt1);

		System.out.println("target size " + aa.size());

		bw.close();
		fileb.close();
		// System.out.println("////"+gasmicodeid[gasi]);
		double a = (double) resulttime;
		double b = (double) resulttime1;
		System.out.println("search done touch data (before IO ) average time " + b + " ms");
		System.out.println("search done (include IO ) average time " + a + " ms");

	}

	public static void main(String[] args)
			throws FileNotFoundException, IOException, ClassNotFoundException, ParseException {

		 String oginaldata = args[0];
	//	String oginaldata = "testdata1.csv";
		Query q = new Query();
		long a1=System.currentTimeMillis();
		q.processing_file(oginaldata);
		long a2=System.currentTimeMillis();
System.out.println("recover index file "+(a2-a1)+" ms");
		query_range = new int[check_keyl][2];

		for (int i = 0; i < check_keyl; i++) {
			q.setRange(i, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}

		int[] other_field;// = new int[] { 2, 0, 3 };
		String input2 =args[1];// "testdata1.csv";
		// String input3 ="10";//args[2];// "testdata1.csv";
		
	//	String input2 = "query_topK_parameter.txt";
		Path file = Paths.get(input2);
		r = 100000;
		InputStream in = Files.newInputStream(file);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

			String line = null;
			int i = 0;

			// StringBuilder response = new StringBuilder();

			line = br.readLine();

			if (line.indexOf("@") >= 0) {
				line = br.readLine();

				// System.out.println(line);
				String[] t = line.split(",");
				int k = Integer.parseInt(args[2]);
				int lo_i = Integer.parseInt(t[0]);
				int la_i = Integer.parseInt(t[1]);
				line = br.readLine();
				line = br.readLine();
				t = line.split(",");

				other_field = new int[t.length];
				while (i < t.length) {
					other_field[i] = Integer.parseInt(t[i]);
					i++;
				}
				while ((line = br.readLine()) != null) {

					line = br.readLine();

					if (line == null || line == "")
						break;
					System.out.println(line);
					t = line.split(",");
					int te_check = Integer.parseInt(t[0]);
					switch (check_key[te_check]) {
					case 0:
						q.setRange(te_check, Integer.parseInt(t[1]), Integer.parseInt(t[2]));
						break;
					case 1:
						q.setRange(te_check, float2Int(Float.parseFloat(t[1])), float2Int(Float.parseFloat(t[2])));
						break;
					case -1:
						SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						timeformat.setTimeZone(TimeZone.getTimeZone("EST"));
						// System.out.println(k);
						Date date = timeformat.parse(t[1]);

						long ti = date.getTime() / 1000;
						Date date2 = timeformat.parse(t[2]);

						long tii = date2.getTime() / 1000;

						q.setRange(te_check, (int) ti, (int) tii);

						break;

					}
				}
				
				r = 100000;
				//i=0;
				//while(i++<10) {
				q.search_topK(k, lo_i, la_i, other_field, oginaldata);
				//}

			} else {
				line = br.readLine();

				// System.out.println(line);
				String[] t = line.split(",");
				other_field = new int[t.length];
				while (i < t.length) {
					other_field[i] = Integer.parseInt(t[i]);
					i++;
				}
				while ((line = br.readLine()) != null) {

					line = br.readLine();

					if (line == null || line == "")
						break;
					System.out.println(line);
					t = line.split(",");
					int te_check = Integer.parseInt(t[0]);
					switch (check_key[te_check]) {
					case 0:
						q.setRange(te_check, Integer.parseInt(t[1]), Integer.parseInt(t[2]));
						break;
					case 1:
						q.setRange(te_check, float2Int(Float.parseFloat(t[1])), float2Int(Float.parseFloat(t[2])));
						break;
					case -1:
						SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						timeformat.setTimeZone(TimeZone.getTimeZone("EST"));
						// System.out.println(k);
						Date date = timeformat.parse(t[1]);

						long ti = date.getTime() / 1000;
						Date date2 = timeformat.parse(t[2]);

						long tii = date2.getTime() / 1000;

						q.setRange(te_check, (int) ti, (int) tii);

						break;

					}
				}
				//i=0;
				//while(i++<10) {
				q.search_range(other_field, oginaldata);
				//}
			}

		}

	}

}
