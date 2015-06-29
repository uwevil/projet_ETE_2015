package simulateur;

import java.io.FileNotFoundException;

import node.Filter;

public class Simulateur {
	public static final int ROOT = 0;
	public static final int SIZE = 512;
	public static final int FRAGMENTS = 64;
	public static final int LIMIT = 1000;
	public static final String DIRECTORY = "/Users/dcs/vrac/test/";
	public Simulateur() 
	{
	}

	public static void main(String[] args) 
	{
		try 
		{
			ReadFile r2 = new ReadFile("/Users/dcs/vrac/test/bf_cab.csv");
			
			System.out.println(r2.size());
			
			long time = System.currentTimeMillis();

			ListFilter list = new ListFilter();

			for (int i = 0; i < r2.size(); i++)
			{
				Filter f = new Filter(r2.getDescription(i), 512);
				list.put(f);
			}
			
			System.out.println("Temps de création l'ensemble de filtres: " 
							+(System.currentTimeMillis() - time)/(1000) + " s");

			System.out.println(list.size());			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
	}
}
