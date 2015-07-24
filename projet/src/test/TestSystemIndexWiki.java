package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import systeme.BF;
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
				if (tmp.length >= 2 && tmp[1].length() > 2)
				{
					BF bf = new BF(sizeOfBF, sizeOfBF/numberOfFragment);
					bf.addAll(tmp[1]);
					systemIndex.add(bf);
					i++;
				}
				
				if (i > 30000)
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
	
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		String requete = "view";
		bf.addAll(requete);
		//String tmp = "11100100100010000100000000010010111000001010100101010000010010100000010000001001110100000000000101001110001000000110010100100010010001101101010110111000110001000010001100101000001100000001011000100101100010001001010110101010010100000000000011101000000110001000111000000000011000001000100000010011011111000100100010011001101010010010000110011000010000001000000000000010100110011010000001011000100001000011001110000100000001000100000000001110001000101000001010100010000000000001000001000000010100000100011000110100";
		//BF bf = new BF(tmp, systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		System.out.println(bf.toString());
		
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/"+ systeme.Configuration.date +"-search_Wiki", false);
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
				+ "Nombre de filtres crées : " + i + "\n"
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
