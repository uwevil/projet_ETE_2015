package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndexCentral;
import exception.ErrorException;

public class TestSystemIndexWiki {
	public static int nodeVisited = 0;
	public static int nodeMatched = 0;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	private static int sizeOfBF = 512;
	private static int numberOfFragment = 64;
	private static int gamma = 2;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ErrorException 
	{
		SystemIndexCentral systemIndex = new SystemIndexCentral(0, 0, gamma);
		long time = System.currentTimeMillis();

		//WriteFile w = new WriteFile("/Users/dcs/vrac/test/bf_description_Wiki", false);
		int i = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60_aa")))
		{
			String s;
			while ((s = reader.readLine()) != null)
			{
				String[] tmp = s.split(";");
				if (tmp.length >= 2 && tmp[1].length() > 3)
				{
					BF bf = new BF(sizeOfBF, sizeOfBF/numberOfFragment);
					bf.addAll(tmp[1]);
					systemIndex.add(bf);
					i++;
				}
				
				if (i > 300)
					break;
				
			//	w.write(bf.toString() + ";" + tmp[1] + "\n");
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
		System.out.println("Nombre de filtres crées : " + i);
	//	w.close();
		
		System.out.println("Temps de création l'ensemble de filtres: " 
						+(System.currentTimeMillis() - time)/(1000) + " s");
		
		System.out.println("systemIndex size = " + systemIndex.size() + " nœuds");
		
		System.out.println("Test Serialization");
		//Serializer serializable = new Serializer();
		//SystemIndexCentral systemIndex = 
			//	(SystemIndexCentral) serializable.readObject("/Users/dcs/vrac/test/23-07-serializer_Wiki");
		
		//serializable.writeObject(systemIndex, "/Users/dcs/vrac/test/23-07-serializer_Wiki");
		
		System.out.println("Serializable OK");
	
		//BF bf = new BF(systeme.Configuration.sizeOfBF, 
	//			systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		String requete = "view";
		//bf.addAll(requete);
		String tmp = "00000000010000000000000100001000000000000000000000000010010100000000000000101001010001000000000000000000010000000000000100100000000000000100000100001000000000000010000000100000000010000000001000100000000000000000000000000000001000000000000000000010000000000000010000010000000000000000000000000000000000000000000101000000000100100010000000000100000000000000000000000000000000000000000000000000000000000000100000000000000000010011000000000000000000000000000000000000000000000000000000001000000000000000001000000000";
		BF bf = new BF(tmp, systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		System.out.println(bf.toString());
		
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/23-07-search_Wiki", false);
		String s = null;
		
		String s2 = "/";
		for (int j = 0; j < systeme.Configuration.numberOfFragment; j++)
		{
			s2 += bf.getFragment(j).toInt();
			if (j < systeme.Configuration.numberOfFragment - 1)
				s2 += "/";
		}
		
		time = System.currentTimeMillis();
		
		Object resultat = systemIndex.search(bf);
		
		s = "Requete : " + requete + "\n"
				+ "RequeteBF : " + bf.toString() + "\n"
				+ "Chemin de BF : " + s2 + "\n\n"
				+ "Temps de recherche: " + (System.currentTimeMillis() - time) + " ms\n" 
				+ "Nœuds total : " + systemIndex.size() + "\n"
				+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
				+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n"
				+ "Trouvé : " + (resultat == null ? 0 : ((ArrayList<BF>) resultat).size()) + " filtres\n\n";
		
		wf.write(s);
		wf.write(systeme.Configuration.nodeMatched + "\n\n");
		wf.write(((resultat == null) ? "0" : ((ArrayList<BF>) resultat).toString()));
		wf.close();
		
		System.out.println("Finish");
		
	}
}
