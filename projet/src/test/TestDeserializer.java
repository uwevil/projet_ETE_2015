package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndex;

public class TestDeserializer {
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		long time = System.currentTimeMillis();

		System.out.println("       Désérializer");
		Serializer serializer = new Serializer();
		SystemIndex systemIndex = (SystemIndex)serializer.readObject("/Users/dcs/vrac/test/serializer_Wiki");
		
		System.out.println("Temps de désérilisation = " + (System.currentTimeMillis() - time)/(1000) + " s\n");
		
		String requete = "oregon,5392,2010";
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		bf.addAll(requete);
		
		time = System.currentTimeMillis();
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
		
		String s = systemIndex.overView();
		
		time = System.currentTimeMillis();
		PrintWriter pw = new PrintWriter("/Users/dcs/vrac/test/overview-Wiki-11-07-"
				+ systeme.Configuration.sizeOfBF + "_"
				+ systeme.Configuration.numberOfFragment + "_"
				+ systeme.Configuration.gamma
				+ "");
		pw.print(s);
		pw.close();
		
		System.out.println("Temps d'écriture: " 
				+ (System.currentTimeMillis() - time)/(1000) + " s");
	}

}
