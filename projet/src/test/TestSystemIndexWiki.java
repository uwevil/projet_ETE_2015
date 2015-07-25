package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import systeme.BF;
import systeme.SystemIndexCentral;
import exception.ErrorException;

@SuppressWarnings("unused")
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
		SystemIndexCentral systemIndex = new SystemIndexCentral(0, 0, gamma);
		long time = System.currentTimeMillis();

		//WriteFile w = new WriteFile("/Users/dcs/vrac/test/bf_description_Wiki", false);
		int i = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60")))
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
				if ( i == 30000)
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
		
		//System.out.println("Test Serialization");
		//Serializer serializable = new Serializer();
		//SystemIndexCentral systemIndex = 
			//	(SystemIndexCentral) serializable.readObject("/Users/dcs/vrac/test/23-07-serializer_Wiki");
		
		//serializable.writeObject(systemIndex, "/Users/dcs/vrac/test/"+ systeme.Configuration.date + "-serializer_Wiki");
		
		System.out.println("Serializable OK");
	
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		String requete = "this,list,characters,ayn,rands,novel,atlas,shrugged";
		bf.addAll(requete);
		//String tmp = "10000001000011111011111100101101100001110000111100000001101101010000011000000110000010011000000010100000100100101001000101000101011000011101101001111000010110001100010001010000111000000000100110100111000001010100101010011010010100000100101000000000000110101001000000101011010101000100000011100111000000001101000011001000001001110010000011011101000010010010010000011001010010100010001001010100000101010010000000000000011010000110001010011010100000010100101101100001000010001001001110001000100110110000011000100001";
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
		
		//Object resultat = systemIndex.search(bf);
		BF resultat = (BF) systemIndex.searchExact(bf);

		
		s = "Requete : " + requete + "\n"
				+ "RequeteBF : " + bf.toString() + "\n"
				+ "Chemin de BF : " + s2 + "\n\n"
				+ "Temps de recherche: " + (System.currentTimeMillis() - time) + " ms\n" 
				+ "Nombre de filtres crées : " + i + "\n"
				+ "Nœuds total : " + systemIndex.size() + "\n"
				+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
				+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n"
				//+ "Trouvé : " + (resultat == null ? 0 : ((ArrayList<BF>) resultat).size()) + " filtres\n\n";
				+ "Trouvé : " + (resultat == null ? 0 : ((BF) resultat).toString()) + "\n\n";

				
		wf.write(s);
		wf.write(systeme.Configuration.nodeMatched + "\n\n");
		//wf.write(((resultat == null) ? "0" : ((ArrayList<BF>) resultat).toString()));
		wf.write(((resultat == null) ? "0" : ((BF) resultat).toString()));
		wf.close();
		
		System.out.println("Finish");
		
	}
}
