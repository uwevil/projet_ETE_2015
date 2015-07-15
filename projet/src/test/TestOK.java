package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndex;

public class TestOK {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		long time = System.currentTimeMillis();

		System.out.println("       Désérializer");
		Serializer serializer = new Serializer();
		SystemIndex systemIndex = (SystemIndex)serializer.readObject("/Users/dcs/vrac/test/serializer_Wiki-15-07");
		
		System.out.println("Temps de désérialisation = " + (System.currentTimeMillis() - time)/(1000) + " s\n");
		
		time = System.currentTimeMillis();
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/15-07-TestOK(0)", false);
		int i = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/bf_description_Wiki")))
		{
			String s;
			while ((s = reader.readLine()) != null)
			{
				String[] tmp = s.split(";");
				BF bf = new BF(systeme.Configuration.sizeOfBF,
						systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
				bf.addAll(tmp[2]);
				BF resultat = (BF) systemIndex.searchExact(bf);
				
				wf.write(resultat.toString() + ";" + tmp[1] + ";" + tmp[2]
						+ ";" +tmp[3] + ";" +tmp[4] + ";" +tmp[5] +"\n");
				i++;
			}
			reader.close();
			wf.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Nombre de filtres : " + i + "\nTemps d'écriture : " 
					+ (System.currentTimeMillis() - time)/(1000) + " s\n");
	}

}
