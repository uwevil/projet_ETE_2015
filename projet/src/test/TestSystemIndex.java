package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import systeme.BF;
import systeme.SystemIndex;

public class TestSystemIndex {

	public static int numberOfBF = 0;
	private static int sizeOfBF = 512;
	private static int numberOfFragment = 64;
	private static int gamma = 1;
	
	public static void main(String[] args) 
	{
		try 
		{
			ReadFile r2 = new ReadFile("/Users/dcs/vrac/test/bf_cab.csv");
			
			System.out.println(r2.size());
			
			SystemIndex systemIndex = new SystemIndex(0, gamma);
			
			long time = System.currentTimeMillis();

			for (int i = 0; i < r2.size(); i++)
			{
				BF bf = new BF(sizeOfBF, sizeOfBF/numberOfFragment);
				bf.addAll(r2.getDescription(i));
				systemIndex.add(bf);
			}
			
			System.out.println("Temps de création l'ensemble de filtres: " 
							+(System.currentTimeMillis() - time)/(1000) + " s");

			System.out.println("systemIndex size = " + systemIndex.size());
			
			/*PrintWriter pw = new PrintWriter("/Users/dcs/vrac/test/resultat-overview-09-07-"
					+ sizeOfBF + "_"
					+ numberOfFragment + "_"
					+ gamma
					+ ".txt");
			pw.print(sizeOfBF + " " + numberOfFragment + " " + gamma + "\n"
					+ "systemIndex size = " + systemIndex.size() + "\n\n" + systemIndex.overView());
			pw.close();
			*/
			systemIndex.overView();
			System.out.println("nombre de filtres = " + numberOfBF);
						
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
	}
}
