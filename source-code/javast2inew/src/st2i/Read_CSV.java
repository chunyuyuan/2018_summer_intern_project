package st2i;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import java.util.Stack;
import java.util.TimeZone;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class Read_CSV {

	// int[] check_key = { 1, 1, 0 };

	// int[] target_key = { 15, 16, 31 };
	int[] check_key;
	int[] target_key;

	Read_CSV(int[] check_key, int[] target_key) {
		this.check_key = check_key;
		this.target_key = target_key;

	}

	Read_CSV(int[] check_key) {
		this.check_key = check_key;

	}

	// static ArrayList<wpoi> atest=new ArrayList<wpoi>();

	public static byte[] long2ByteArray(long value) {
		return ByteBuffer.allocate(8).putLong(value).array();
	}

	public static byte[] float2ByteArray(float value) {
		return ByteBuffer.allocate(4).putFloat(value).array();
	}

	public static byte[] int2ByteArray(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public static byte[] double2ByteArray(double value) {
		return ByteBuffer.allocate(8).putDouble(value).array();
	}

/*	public static int float2Int(float i) {
		int te = (int) (i * 1000000);
		return te;
	}*/
	
	public static int float2Int(float i) {
		return (int) (i * 10000);
	}

	

	/*public static float int2Float(float i) {
		float te = (float) (i / 1000000);
		return te;
	}*/
	//use byte array to convert
	public static float int2Float(int i) {
		return (float) (i / 10000);
	}
	   public static boolean isWindows()
	   {
		   String os=System.getProperty("os.name");
		  
	      return os.startsWith("Windows");
	   }

	public ArrayList<key_value> processCSVFile(String input, String output1) throws IOException, ParseException {

		int byteL = 0;
		
		ArrayList<key_value> key_array = new ArrayList<key_value>();
		int i_key;
		int co = 0;
		//OutputStream in = Files.newOutputStream(arg0, arg1).newInputStream(file);
		while (co < 1) {
			Path file = Paths.get(input);
			long a =0;
			RandomAccessFile raf = new RandomAccessFile(input, "rw");
			//RandomAccessFile raf1 = new RandomAccessFile(input, "rw");
				
			
			InputStream in = Files.newInputStream(file);
			
			//Path file2;
		
			System.out.println(System.getProperty("os.name"));
	
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

				String line = null;
				int row = 0;

				System.out.println("Start");

				FileOutputStream out1 = new FileOutputStream(output1);

		

				while ((line = br.readLine()) != null) {
					//line=br.readLine();
				/*	if(isWindows()) {
					//	if (row == 0) 
							a += line.length()+2 ;
					}else {
						bw.write(line);
					//	if (row == 0) 
							a += line.length();
						//bw.newLine();
					}*/
				//	long a1=a;
					
					if(isWindows()) {
						a += line.length()+2;
						
						
					}else {
					//	a += (line.length()-line.lastIndexOf(","));
						a += line.length()-5;
						raf.seek(a);
						while(raf.read()!=10) {
							//System.out.println("//////");
							a+=1;
						}
						
						a+=1;
					}
					
			if(row==0) {
				ByteBuffer dbuf = ByteBuffer.allocate(8);
				//a += line.length();
				
				dbuf.putLong(a);
				// System.out.println(dbuf.getLong());

				out1.write(dbuf.array());
			}
				
					

					// int ii = 0;

					if (row > 0) {

						CSVParser csvParser = CSVParser.parse(line, CSVFormat.DEFAULT);
						
						 i_key = 0;
						int[] temp = new int[this.check_key.length + 1];
						for (CSVRecord csvRecord : csvParser) {
						//	System.out.println(csvRecord.toString());
							while (i_key < this.check_key.length) {
								String k = csvRecord.get(this.target_key[i_key]);
							//	System.out.println(csvRecord.get(1)+" "+csvRecord.get(2)+" "+csvRecord.get(3)+" ");
								switch (this.check_key[i_key]) {
								case 0:
									temp[i_key] = Integer.parseInt(k);
									/*
									 * if(Integer.parseInt(k)== 10906040) { c++;
									 * //System.out.println("\n 10906040 c "+c+" "+i_key); }
									 */
									break;
								case 1:

									temp[i_key] = float2Int(Float.parseFloat(k));
									break;
									
								case -1:
									
									SimpleDateFormat timeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									timeformat.setTimeZone(TimeZone.getTimeZone("EST"));
									//System.out.println(k);
									Date date=timeformat.parse(k);
								
									long t=date.getTime()/1000;
									
									temp[i_key]=(int)t;
									
									String date1 = timeformat.format(t*1000);
									// temp[i_key]=(int)t;
								//	content .append( "\n" + csvRecord_header.get(target_key[i]) + " " + date.toString());
								//	System.out.println(k+"////////// "+date1.toString());
									break;

								}

								i_key++;

							}

						}

						temp[i_key] = row ;

						// tem.recordIndex=row-1;
						key_value tem = new key_value(temp);
						// oout.writeUnshared(tem);
						key_array.add(tem);
						ByteBuffer dbuf = ByteBuffer.allocate(8);
						//a += line.length();
						
						dbuf.putLong(a);
						// System.out.println(dbuf.getLong());

						out1.write(dbuf.array());
						csvParser.close();

					}
					
					if (row % 5000000 == 0) {

						System.out.println(row + " read done");
						// System.out.println(a);
						
						// System.out.println(bb.getLong());
					}
					row++;
				}
				
				
				System.out.println("StringPosition file size "+out1.getChannel().size()+" bytes");
				
				out1.close();
				out1.flush();
				br.close();
                in.close();
			}

			co++;
		}
		return key_array;

	}

	public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String address = System.getProperty("user.dir");
		String input = "C:\\Users\\yuanchunyu\\Downloads\\.csv";
		// String output = "C:\\Users\\yuanchunyu\\Downloads\\out911.dat";
		String output1 = "C:\\Users\\yuanchunyu\\Downloads\\outindex9.dat";
		System.out.println("write");
		int[] check_key = { 1, 1, 0 };

		int[] target_key = { 15, 16, 31 };
		Read_CSV read_csv = new Read_CSV(check_key, target_key);
	//	read_csv.processCSVFile(input, output1);

		System.out.println("read");
		// readBinaryFile(output);

	}

}
