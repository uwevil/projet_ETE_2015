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
		SystemIndex systemIndex = (SystemIndex)serializer.readObject("/Users/dcs/vrac/test/serializer_Wiki-15-07");
		
		System.out.println("Temps de désérialisation = " + (System.currentTimeMillis() - time)/(1000) + " s\n");
		
		//WriteFile wf = new WriteFile("/Users/dcs/vrac/test/overview_Wiki-15-07", false);
		//wf.write(systemIndex.overView());
		//wf.close();
		
		
		String requete = "elliott,city,ransom,county,north,dakota,united,states,population,2010,census,elliott,founded,1883";
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		//bf.addAll(requete);
		
		
		time = System.currentTimeMillis();
		
		String path = "/Users/dcs/vrac/test/queries/wiki/";
		String fileName = "exactQuriesWDocs";
		ReadFile r = new ReadFile(path + fileName + ".csv");
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/15-07-searchExact-Wiki-"
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
			Object resultat = systemIndex.searchExact(bf);
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

		 
		/*
		System.out.println("        Recherche");
		
		//System.out.println(systemIndex.searchExact(bf));
				
		//systemIndex.remove(bf);
		
		//@SuppressWarnings("unchecked")
		
		String s2 = "/";
		for (int i = 0; i < systeme.Configuration.numberOfFragment; i++)
		{
			s2 += bf.getFragment(i).toInt();
			if (i < systeme.Configuration.numberOfFragment - 1)
				s2 += "/";
		}
			
		time = System.currentTimeMillis();

		//ArrayList<BF> resultat = (ArrayList<BF>)systemIndex.search(bf);
		
		BF resultat = (BF) systemIndex.searchExact(bf);
		
		System.out.println("Temps de recherche: " 
				+ (System.currentTimeMillis() - time)/(1000) + " s\n" 
				+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds");
		
		String s1 = new String("size 0\n\n");
		
		if (resultat != null)
			s1 = "size " + resultat.size() + "\n\n";
		
		//String s1 = systemIndex.overView();
		String s = "Total : " + 0 + " filtres\n"
				+ "Doublon : " + systeme.Configuration.doublon + "\n"
				+ "Effective : " + systeme.Configuration.numberOfBF + "\n\n"
				+ "Requete : " + requete + "\n"
				+ "RequeteBF : " + bf.toString() + "\n"
				+ "Chemin de BF : " + s2 + "\n\n"
				+ "Temps de recherche: " + (System.currentTimeMillis() - time)/(1000) + " s\n" 
				+ "Nœuds total : " + systemIndex.size() + "\n"
				+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
				+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n\n" 
			//	+ s1 + "\n"
				+ systeme.Configuration.nodeMatched;
		
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/15-07-searchExact_Wiki(0)", false);
		wf.write(s);
		wf.close();
		
		System.out.println("Temps d'écriture: " 
				+ (System.currentTimeMillis() - time)/(1000) + " s");
				
		*/
	}

}
