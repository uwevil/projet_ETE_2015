package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import exception.ErrorException;
import systeme.BF;
import systeme.SystemIndex;

public class TestSystemIndex {

	public static int nodeVisited = 0;
	public static int nodeMatched = 0;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	private static int sizeOfBF = 512;
	private static int numberOfFragment = 64;
	private static int gamma = 1000;
	
	public static void main(String[] args) throws ErrorException 
	{
		try 
		{
			ReadFile r2 = new ReadFile("/Users/dcs/vrac/test/bf_cab.csv");
		//	WriteFile w = new WriteFile("/Users/dcs/vrac/test/bf_description", false);
			
			System.out.println(r2.size() + " données lues");
			
			SystemIndex systemIndex = new SystemIndex(0, gamma);
			
			long time = System.currentTimeMillis();

			for (int i = 0; i < r2.size(); i++)
			{
				BF bf = new BF(sizeOfBF, sizeOfBF/numberOfFragment);
				String s = r2.getDescription(i);
				bf.addAll(s);
			//	w.write(bf.toString() + ";" + s + "\n");
				
				systemIndex.add(bf);
			}
			
			//w.close();
			
			System.out.println("Temps de création l'ensemble de filtres: " 
							+(System.currentTimeMillis() - time)/(1000) + " s");

			System.out.println("systemIndex size = " + systemIndex.size() + " nœuds");
			
			//String s = sizeOfBF + " " + numberOfFragment + " " + gamma + "\n"
			//		+ "systemIndex size = " + systemIndex.size() + "\n\n" + systemIndex.overView();
			
			
			String requete = "reference,education,organizations";
			BF bf = new BF(sizeOfBF, sizeOfBF/numberOfFragment);
			bf.addAll(requete);
				
			time = System.currentTimeMillis();
			
			//System.out.println(systemIndex.searchExact(bf));
			
			@SuppressWarnings("unchecked")
			ArrayList<BF> resultat = (ArrayList<BF>)systemIndex.search(bf);
			String s1 = "size " + resultat.size() + "\n\n" + resultat.toString();
			
			//String s1 = systemIndex.overView();
			String s = "Total : " + r2.size() + " filtres\n"
					+ "Doublon : " + doublon + "\n"
					+ "Effective : " + numberOfBF + "\n\n"
					+ "Requete : " + requete + "\n"
					+ "RequeteBF : " + bf.toString() + "\n"
					+ "Temps de recherche: " + (System.currentTimeMillis() - time)/(1000) + " s\n" 
					+ "Nœuds total : " + systemIndex.size() + "\n"
					+ "Nœuds visités : " + nodeVisited + " nœuds\n"
					+ "Nœuds matched : " + nodeMatched + " nœuds\n\n" 
					+ s1;
			
			System.out.println("Temps de recherche: " 
							+ (System.currentTimeMillis() - time)/(1000) + " s\n" 
							+ "Nœuds visités : " + nodeVisited + " nœuds");
			
			time = System.currentTimeMillis();
			PrintWriter pw = new PrintWriter("/Users/dcs/vrac/test/resultat-search-perf-10-07-"
					+ sizeOfBF + "_"
					+ numberOfFragment + "_"
					+ gamma
					+ "(3)");
			pw.print(s);
			pw.close();
			
			System.out.println("Temps d'écriture: " 
					+ (System.currentTimeMillis() - time)/(1000) + " s");
			
			//System.out.println("nombre de filtres = " + numberOfBF);
						
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
	}
}
