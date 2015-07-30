package calculRepartitionBit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import peerSimTest.Config;
import test.WriteFile;

public class Generation_pstricks {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		Hashtable<String, Object> repartition = new Hashtable<String, Object>();
		int[] sang = new int[Config.sizeOfBF];
		int[] bassirou = new int[Config.sizeOfBF];
		
		float[] ps = new float[Config.sizeOfBF];
		float[] pb = new float[Config.sizeOfBF];
		
		System.out.println("Commence");
		Config config_log = new Config();

		int size = 5704141;
		
		int line = 0;
		
		for (int i = 0; i < Config.sizeOfBF; i++)
		{
			sang[i] = 0;
			bassirou[i] = 0;
		}

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
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/29-07_tab_repartition", true);
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

		String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
		
		DecimalFormat df = new DecimalFormat ( ) ; 
		df.setMaximumFractionDigits ( 5 ) ; //arrondi à 2 chiffres apres la virgules 
		df.setMinimumFractionDigits ( 5 ) ; 
		
		for (int i = 0; i < 512; i++)
		{
			ps[i] = (float) (((float) sang[i] / (float) (line*3)) * (float)100);
			pb[i] = (float) (((float) bassirou[i] / (float) (line*3)) * (float)100);
						
			WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_pourcentage_sang.pstricks", true);
			wf.write(i + " " + ps[i] + "\n");
			wf.close();
			
			wf = new WriteFile("/Users/dcs/vrac/test/" + date + "_pourcentage_bassirou.pstricks", true);
			wf.write(i + " " + pb[i] +"\n");
			wf.close();
		}
	}

}
