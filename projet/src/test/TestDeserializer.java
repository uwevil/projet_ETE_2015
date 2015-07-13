package test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndex;

public class TestDeserializer {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		long time = System.currentTimeMillis();

		System.out.println("       Désérializer");
		Serializer serializer = new Serializer();
		SystemIndex systemIndex = (SystemIndex)serializer.readObject("/Users/dcs/vrac/test/serializer_Wiki");
		
		System.out.println("Temps de désérilisation = " + (System.currentTimeMillis() - time)/(1000) + " s\n");
		
		String requete = "";
		BF bf = null;
		
		time = System.currentTimeMillis();
		
		String path = "/Users/dcs/vrac/test/queries/wiki/";
		String fileName = "exactQuriesWDocs";
		ReadFile r = new ReadFile(path + fileName + ".csv");
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/12-07-search-Wiki-"
				+ systeme.Configuration.sizeOfBF + "_"
				+ systeme.Configuration.numberOfFragment + "_"
				+ systeme.Configuration.gamma + "_"
				+ fileName, false);
		
		for (int i = 0; i < r.size(); i++)
		{
			bf = new BF(systeme.Configuration.sizeOfBF, 
					systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
			requete = r.getDescription(i);
			bf.addAll(requete);
			time = System.currentTimeMillis();
			Object resultat = systemIndex.search(bf);
			String s = null;
			
			if (resultat != null)
			{
				s =  "Requete : " + requete + "\n"
						+ "RequeteBF : " + bf.toString() + "\n"
						+ "Temps de recherche: " + (System.currentTimeMillis() - time)/(1000) + " s\n" 
						+ "Nœuds total : " + systemIndex.size() + "\n"
						+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
						+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n\n" 
						+ ((ArrayList<BF>) resultat).size()
						+ "\n\n";
				wf.write(s);
			}else{
				s =  "Requete : " + requete + "\n"
						+ "RequeteBF : " + bf.toString() + "\n"
						+ "Temps de recherche: " + (System.currentTimeMillis() - time)/(1000) + " s\n" 
						+ "Nœuds total : " + systemIndex.size() + "\n"
						+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
						+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n\n" 
						+ "0"
						+ "\n\n";
				wf.write(s);
			}
		}
		
		wf.close();

		//System.out.println("        Recherche");
		
		//System.out.println(systemIndex.searchExact(bf));
				
		//systemIndex.remove(bf);
		
		//@SuppressWarnings("unchecked")

		//ArrayList<BF> resultat = (ArrayList<BF>)systemIndex.search(bf);
		
		//BF resultat = (BF) systemIndex.searchExact(bf);
		
		//String s1 = new String("size 0\n\n");
		
		//if (resultat != null)
		//	s1 = "size " + resultat.size() + "\n\n" + resultat.toString();
		
		//String s1 = systemIndex.overView();
		/*String s = "Total : " + 0 + " filtres\n"
				+ "Doublon : " + systeme.Configuration.doublon + "\n"
				+ "Effective : " + systeme.Configuration.numberOfBF + "\n\n"
				+ "Requete : " + requete + "\n"
				+ "RequeteBF : " + bf.toString() + "\n"
				+ "Temps de recherche: " + (System.currentTimeMillis() - time)/(1000) + " s\n" 
				+ "Nœuds total : " + systemIndex.size() + "\n"
				+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
				+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n\n" 
				+ s1;
		
		System.out.println("Temps de recherche: " 
						+ (System.currentTimeMillis() - time)/(1000) + " s\n" 
						+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds");
		*/
				
		
		System.out.println("Temps d'écriture: " 
				+ (System.currentTimeMillis() - time)/(1000) + " s");
	}

}