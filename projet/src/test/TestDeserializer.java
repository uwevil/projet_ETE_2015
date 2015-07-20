package test;

import java.util.ArrayList;

import systeme.BF;
import systeme.Fragment;
import systeme.Serializer;
import systeme.SystemIndexCentral;

public class TestDeserializer {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long time = System.currentTimeMillis();

		System.out.println("       Désérializer");
		Serializer serializer = new Serializer();
		SystemIndexCentral systemIndex = (SystemIndexCentral)serializer.readObject("/Users/dcs/vrac/test/20-07-serializer");
				
		System.out.println("Temps de désérialisation = " + (System.currentTimeMillis() - time)/(1000) + " s\n");
		
				
		String requete = "world"
				//+ ",français,régional,europe,france,régions"
				//+ ",languedoc-roussillon,gard,commerce,économie,bâtiment"
				//+ ",travaux,publics,regional,business,economy,construction,maintenance,regions"
				;
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
						systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		bf.addAll(requete);
				
				
		time = System.currentTimeMillis();
				
		WriteFile wf1 = new WriteFile("/Users/dcs/vrac/test/20-07-search-Wiki-graph-"
				+ systeme.Configuration.sizeOfBF + "_"
				+ systeme.Configuration.numberOfFragment + "_"
				+ systeme.Configuration.gamma
				+ "(1)"
				//+ fileName
				, false);
		
		time = System.currentTimeMillis();
		Object resultat = systemIndex.search(bf);
		String s = null;
					
	
	String s2 = "/";
	for (int i = 0; i < systeme.Configuration.numberOfFragment; i++)
	{
		s2 += bf.getFragment(i).toInt();
		if (i < systeme.Configuration.numberOfFragment - 1)
			s2 += "/";
	}
	
	s = "Total : " + 0 + " filtres\n"
			+ "Doublon : " + systeme.Configuration.doublon + "\n"
			+ "Effective : " + systeme.Configuration.numberOfBF + "\n\n"
			+ "Requete : " + requete + "\n"
			+ "RequeteBF : " + bf.toString() + "\n"
			+ "Chemin de BF : " + s2 + "\n\n"
			+ "Temps de recherche: " + (System.currentTimeMillis() - time)/(1000) + " s\n" 
			+ "Nœuds total : " + systemIndex.size() + "\n"
			+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
			+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds"
			+ "Trouvé : " + (resultat == null ? 0 : ((ArrayList<BF>) resultat).size()) + "\n\n";
	
	
	wf1.write(s);
	wf1.write(systeme.Configuration.nodeMatched + "\n\n");
	
	/*
	String s3 = "";
	for (int i = 0; i < systeme.Configuration.graph.size(); i ++)
	{
		Fragment f = bf.getFragment(i);
		s3 = " Rang " + i + " : /" + f.toString() + "\n  " + systeme.Configuration.graph.get(i).toString() + "\n";
		wf1.write(s3);
		s3 = "  " + systeme.Configuration.dispo.get(i).toString() + "\n";
		wf1.write(s3);
	}
	*/
	
	wf1.close();			

	System.out.println("End");	 
				
	}
	

}
