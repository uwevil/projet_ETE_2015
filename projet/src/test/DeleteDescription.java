package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndexCentral;

public class DeleteDescription {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("******Commence******");
		long time = System.currentTimeMillis();
		
		/*
		try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs (1).csv")))
		{
			reader.readLine();
			String s;
			while ((s = reader.readLine()) != null)
			{
				String[] tmp = s.split(";");
				
				if (tmp[1] != "")
				{
					String[] tmp2 = tmp[1].split(",");
					
					/*
					if (tmp2.length >= 60)
					{
						WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs>=60", true);
						wf.write("//" +tmp2.length + "//;" + tmp[0] + "\n");
						wf.close();
					}else 
						*/
					/*	
					if (tmp2.length > 0)
					{
						WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs<60", true);
						wf.write(tmp[0] + ";" + tmp[1] + "\n");
						wf.close();
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Fini la suppression : " 
				+(System.currentTimeMillis() - time)/(1000) + " s");
		
		*/
		System.out.println("******Création systemIndexCentral******");

		time = System.currentTimeMillis();
		
		SystemIndexCentral systemIndexCentral = new SystemIndexCentral(0, 0, systeme.Configuration.gamma);
		try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60")))
		{
			String s;
			while ((s = reader.readLine()) != null)
			{
				try
				{
					String[] tmp = s.split(";");
					BF bf = new BF(systeme.Configuration.sizeOfBF, 
							systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
					
					bf.addAll(tmp[1]);
					systemIndexCentral.add(bf);		
				}catch (ArrayIndexOutOfBoundsException e)
				{
						System.out.println(s);
				}
				
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Fini la création systemIndexCentral : " 
				+(System.currentTimeMillis() - time)/(1000) + " s");
		
		System.out.println("******Sérialisation******");

		time = System.currentTimeMillis();

		Serializer serializer = new Serializer();
		serializer.writeObject(systemIndexCentral, "/Users/dcs/vrac/test/21-07-serializer-wikiDocs<60");
		System.out.println("Fini la sérialisation : " 
				+(System.currentTimeMillis() - time)/(1000) + " s");
		System.out.println("end");
	}

}
