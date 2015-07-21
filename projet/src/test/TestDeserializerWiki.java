package test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import systeme.BF;
import systeme.Serializer;
import systeme.SystemIndexCentral;

public class TestDeserializerWiki {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		long time = System.currentTimeMillis();

		System.out.println("       Désérializer");
		Serializer serializer = new Serializer();
		SystemIndexCentral systemIndex = 
				(SystemIndexCentral)serializer.readObject("/Users/dcs/vrac/test/20-07-serializer-wikiDocs<60");
		
		System.out.println("Temps de désérialisation = " + (System.currentTimeMillis() - time)/(1000) + " s\n");
		
		//systemIndex.toFile("/Users/dcs/vrac/test/20-07-toString-wikiDocs<60", false);
		
		/*
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/20-07-overViews-wikiDocs<60", false);
		wf.write(systemIndex.toString());
		wf.close();
		*/
		
		
		String requete = "sahib,titles,hymns,give,rg";
				//+ ",backgroundcolorlavenderstructurethtrtrtd,spanrow,styletextalignleft"
				//+ ",backgroundcolortransparentassemblysenate"
				//+ ",seatshouse,seatstdtd236,157,180,119,tdtrtrtd"
				//+ ",spanrow,styletextalignleft,backgroundcolortransparentpolitical";
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		//bf.addAll(requete);
		
		
		time = System.currentTimeMillis();
		
		int numberOfTest = 3;
		
	//	String path = "/Users/dcs/vrac/test/queries/wiki/";
		//String fileName = "exactQuriesWDocs";
		//ReadFile r = new ReadFile(path + fileName + ".csv");
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/20-07-search-Wiki-"
				+ numberOfTest, false);
		
		//for (int i = 0; i < r.size(); i++)
	//	{
			//bf = new BF(systeme.Configuration.sizeOfBF, 
			//		systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
			//requete = r.getDescription(i);
			bf.addAll(requete);
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
	
	//	}
		
		wf.close();
		
		WriteFile wf1 = new WriteFile("/Users/dcs/vrac/test/20-07-search-Wiki-graph-"
				+ numberOfTest, false);
		for (int i = 0; i < systeme.Configuration.graph.size(); i++)
		{
			wf1.write("rang " + i + " : \n" + systeme.Configuration.graph.get(i) + "\n\n");
		}
		wf1.close();

		 
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
		
		*/
		System.out.println("Temps d'écriture: " 
				+ (System.currentTimeMillis() - time)/(1000) + " s");
				
	}

}
