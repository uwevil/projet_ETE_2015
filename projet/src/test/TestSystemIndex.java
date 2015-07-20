package test;

import java.io.FileNotFoundException;

import exception.ErrorException;
import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndexCentral;

public class TestSystemIndex {
	
	public static void main(String[] args) throws ErrorException 
	{
		try 
		{
			ReadFile r2 = new ReadFile("/Users/dcs/vrac/test/bf_cab.csv");
			
			System.out.println(r2.size() + " données lues");
			
			SystemIndexCentral systemIndex = new SystemIndexCentral(0, 0, systeme.Configuration.gamma);
			
			long time = System.currentTimeMillis();

			for (int i = 0; i < r2.size(); i++)
			{
				BF bf = new BF(systeme.Configuration.sizeOfBF, 
						systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
				String s = r2.getDescription(i);
				bf.addAll(s);
				
				systemIndex.add(bf);
			}
						
			System.out.println("Temps de création l'ensemble de filtres: " 
							+(System.currentTimeMillis() - time)/(1000) + " s");

			System.out.println("systemIndex size = " + systemIndex.size() + " nœuds");
				
			time = System.currentTimeMillis();
			
			
			
			System.out.println("Temps de recherche: " 
							+ (System.currentTimeMillis() - time)/(1000) + " s\n" 
							+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds");
			
			time = System.currentTimeMillis();
			
			
			
			System.out.println("Temps d'écriture: " 
					+ (System.currentTimeMillis() - time)/(1000) + " s");

			System.out.println("Test Serialization");
			Serializer serializable = new Serializer();
			serializable.writeObject(systemIndex, "/Users/dcs/vrac/test/20-07-serializer");
			System.out.println("Serializable OK");
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
	}
}
