package calculRepartitionBit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import peerSimTest_v1.Config;
import test.WriteFile;

@SuppressWarnings("unused")
public class CalculRepartitionBit {

	// repartition contient String pour une table 6 entrées: h1 h2 h3 b1 b2 b3
	public static Hashtable<String, Object> repartition = new Hashtable<String, Object>();
	public static int[] sang = new int[Config.sizeOfBF];
	public static int[] bassirou = new int[Config.sizeOfBF];
	
	public static float[] ps = new float[Config.sizeOfBF];
	public static float[] pb = new float[Config.sizeOfBF];
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		System.out.println("Commence");
		Config config_log = new Config();

		int size = 5704141;
		
		int line = 0;
		
		for (int i = 0; i < Config.sizeOfBF; i++)
		{
			sang[i] = 0;
			bassirou[i] = 0;
		}
		
		String date = (new SimpleDateFormat("dd-MM-yyyy/")).format(new Date());

		try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikDistinctTerm.csv")))
		{	
			 reader.readLine();
			 reader.readLine();
			 reader.readLine();
			
			while (true)
			{
				String s = new String();
				s = reader.readLine();
				if (s == null)
					break;
							
				BF_test bf = new BF_test(config_log.sizeOfBF);
				BBF_test bbf = new BBF_test(config_log.sizeOfBF);
				
				bf.add(s);
				bbf.hash(s);
				
				int[] o = new int[6];
				
				o[0] = bf.get0();
				o[1] = bf.get1();
				o[2] = bf.get2();
				
				o[3] = bbf.get0();
				o[4] = bbf.get1();
				o[5] = bbf.get2();
				
				sang[bf.get0()]++;
				sang[bf.get1()]++;
				sang[bf.get2()]++;
				
				bassirou[bbf.get0()]++;
				bassirou[bbf.get1()]++;
				bassirou[bbf.get2()]++;
								
				repartition.put(s, o);
				/*
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_tab_repartition", true);
				wf.write(s + ";" + o[0] 
						   + "," + o[1]
						   + "," + o[2]
		     			   + "," + o[3]			
		     			   + "," + o[4]							   
						   + "," + o[5]
						   + "\n");
				wf.close();
				*/
				line++;
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("??? line == size??? " + line + " ?= " + size);
		
		//Serializer serializer = new Serializer();
		//serializer.writeObject(repartition, "/Users/dcs/vrac/test/29-07_serializer_repartition");

		date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
		
		
		float f = 0, f2 = 0;
		for (int i = 0; i < 512; i++)
		{
			ps[i] = (float) (((float) sang[i] / (float) (line*3)) * (float)100);
			pb[i] = (float) (((float) bassirou[i] / (float) (line*3)) * (float)100);
			
			f += ps[i];
			f2 += pb[i];
			
			WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_pourcentage_sang", true);
			if (i < 10)
			{
				wf.write(i + "   " + ps[i] + " " + sang[i] + "\n");
			}
			else if (i >= 10 && i < 100)
			{
				wf.write(i + "  " + ps[i] + " " + sang[i] + "\n");
			}
			else
			{
				wf.write(i + " " + ps[i] + " " + sang[i] + "\n");
			}
			wf.close();
			
			wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_pourcentage_bassirou", true);
			if (i < 10)
			{
				wf.write(i + "   " + pb[i] + " " + bassirou[i] + "\n");
			}
			else if (i >= 10 && i < 100)
			{
				wf.write(i + "  " + pb[i] + " " + bassirou[i] + "\n");
			}
			else
			{
				wf.write(i + " " + pb[i] + " " + bassirou[i] + "\n");
			}
			wf.close();
		}
		
		WriteFile wf1 = new WriteFile("/Users/dcs/vrac/test/" + date + "_pourcentage_sang", true);
		wf1.write("Nombre total de mots : " + line + "\n");
		wf1.write("Pourcentage total : " + f + "%\n");
		wf1.write("Pourcentage moyen : " + f + "/" + 512 + " ~ " + (f/line) + "%\n");
		wf1.close();
		
		wf1 = new WriteFile("/Users/dcs/vrac/test/" + date + "_pourcentage_bassirou", true);
		wf1.write("Nombre total de mots : " + line + "\n");
		wf1.write("Pourcentage total : " + f2 + "%\n");
		wf1.write("Pourcentage moyen : " + f2 + "/" + 512 + " ~ " + (f2/line) + "%\n");
		wf1.close();
		
		
		Hashtable<String, Integer> hs = new Hashtable<String, Integer>();
		Hashtable<String, Integer> hb = new Hashtable<String, Integer>();
		
		Enumeration<String> enumeration = repartition.keys();
		
		while (enumeration.hasMoreElements())
		{
			String s = enumeration.nextElement();
			int[] a = (int[]) repartition.get(s);
			
			Enumeration<String> enumeration2 = repartition.keys();
			
			while (enumeration2.hasMoreElements())
			{
				String s2 = enumeration2.nextElement();
				
				if (s == s2)
					continue;
				
				int[] b = (int[]) repartition.get(s2);
				
				int[] tmp1 = new int[6];
				int[] tmp2 = new int[6];
				
				tmp1[0] = a[0];
				tmp1[1] = a[1];
				tmp1[2] = a[2];
				tmp1[3] = b[0];
				tmp1[4] = b[1];
				tmp1[5] = b[2];
				
				tmp2[0] = a[3];
				tmp2[1] = a[4];
				tmp2[2] = a[5];
				tmp2[3] = b[3];
				tmp2[4] = b[4];
				tmp2[5] = b[5];
				
				if (testDup(tmp1) && !hs.containsKey(s2))
				{
					hs.put(s2, 11);
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_DUP_sang", true);
					wf.write(s + ";" 
						   + s2 + ";" 
						   +       tmp1[0]
					       + "," + tmp1[1]
						   + "," + tmp1[2]
						   + ";" + tmp1[3]
						   + "," + tmp1[4]
						   + "," + tmp1[5]
						   + "\n");
					wf.close();
				}
				
				if (testDup(tmp2) && !hb.containsKey(s2))
				{
					hb.put(s2, 11);
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_DUP_bassirou", true);
					wf.write(s + ";" 
						   + s2 + ";" 
						   +       tmp2[0]
					       + "," + tmp2[1]
						   + "," + tmp2[2]
						   + ";" + tmp2[3]
			 			   + "," + tmp2[4]
			 			   + "," + tmp2[5]
						   + "\n");
					wf.close();
				}	
			}
			hs.put(s, 11);
			hb.put(s, 11);

		}
		
		Enumeration<String> enumeration3 = hs.keys();
		
		int k = 0;
		while (enumeration3.hasMoreElements())
		{
			enumeration3.nextElement();
			k++;
		}
		
		System.out.println("End " + line + " ?= " + k);
		
	//	System.out.println("End " + line);
		
	}
	
	private static boolean testDup(int[] o)
	{		
		int min1, min2;
		
		for (int i = 0; i < 3; i++)
		{
			min1 = o[i];
			min2 = o[i+3];
			
			for (int j = i + 1; j < 3; j++)
			{
				if (min1 > o[j])
				{
					int tmp = min1;
					min1 = o[j];
					o[j] = tmp;
				}
				
				if (min2 > o[j+3])
				{
					int tmp = min2;
					min2 = o[j+3];
					o[j+3] = tmp;
				}
			}
			o[i] = min1;
			o[i+3] = min2;
		}
			
		for (int i = 0; i < 3; i++)
		{
			if (o[i] != o[i+3])
				return false;
		}
		
		return true;
	}

}
