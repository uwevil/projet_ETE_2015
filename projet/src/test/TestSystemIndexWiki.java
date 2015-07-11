package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndex;
import exception.ErrorException;

public class TestSystemIndexWiki {
	public static int nodeVisited = 0;
	public static int nodeMatched = 0;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	private static int sizeOfBF = 512;
	private static int numberOfFragment = 64;
	private static int gamma = 1000;
	
	public static void main(String[] args) throws ErrorException 
	{
		//SystemIndex systemIndex = new SystemIndex(0, gamma);
		long time = System.currentTimeMillis();

		WriteFile w = new WriteFile("/Users/dcs/vrac/test/bf_description_Wiki", false);
		int i = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs (1).csv")))
		{
			reader.readLine();
			String s;
			while ((s = reader.readLine()) != null)
			{
				String[] tmp = s.split(";");
				BF bf = new BF(sizeOfBF, sizeOfBF/numberOfFragment);
				bf.addAll(tmp[1]);
			//	systemIndex.add(bf);
				
				w.write(bf.toString() + ";" + s + "\n");
				i++;
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
		System.out.println("Nombre de filtres crées : " + i);
		w.close();
		
		System.out.println("Temps de création l'ensemble de filtres: " 
						+(System.currentTimeMillis() - time)/(1000) + " s");

	//	System.out.println("systemIndex size = " + systemIndex.size() + " nœuds");
		
		System.out.println("Test Serialization");
		//Serializer serializable = new Serializer();
	//	serializable.writeObject(systemIndex, "/Users/dcs/vrac/test/serializer_Wiki");
		System.out.println("Serializable OK");
	
	}
}
