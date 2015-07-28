package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	private static int gamma = 1000;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ErrorException 
	{
		SystemIndexCentral systemIndex = new SystemIndexCentral(0, 0, gamma);
		long time = System.currentTimeMillis();

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
				//if ( i == 30000)
					//break;
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
		System.out.println("Nombre de filtres crées : " + i);
		
		System.out.println("Temps de création l'ensemble de filtres: " 
						+(System.currentTimeMillis() - time)/(1000) + " s");
		
		System.out.println("systemIndex size = " + systemIndex.size() + " nœuds");
		
		//System.out.println("Test Serialization");
		//Serializer serializable = new Serializer();
		//SystemIndexCentral systemIndex = 
			//	(SystemIndexCentral) serializable.readObject("/Users/dcs/vrac/test/23-07-serializer_Wiki");
		
		//serializable.writeObject(systemIndex, "/Users/dcs/vrac/test/"+ systeme.Configuration.date + "-serializer_Wiki");
		
		System.out.println("Serializable OK");
	
		String requete = "";
		BF bf = null;
		try {
			ReadFile rf = new ReadFile("/Users/dcs/vrac/test/wikiDocs<60_500_request");
			
			for (int j = 0; j < 1; j++) //rf.size(); i++)
			{
				bf = new BF(systeme.Configuration.sizeOfBF, 
						systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
				
				requete = rf.getDescription(j);
				bf.addAll(requete);
				
			}
			System.out.println("NOMBRE de requete = " + rf.size());
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
		
		System.out.println(date);
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/"+ date +"_search_Wiki", false);
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
		ArrayList<BF> resultat = (ArrayList<BF>) systemIndex.search(bf);
		
		s = "Requete : " + requete + "\n"
				+ "RequeteBF : " + bf.toString() + "\n"
				+ "Chemin de BF : " + s2 + "\n\n"
				+ "Temps de recherche: " + (System.currentTimeMillis() - time) + " ms\n" 
				+ "Nombre de filtres crées : " + i + "\n"
				+ "Nœuds total : " + systemIndex.size() + "\n"
				+ "Nœuds visités : " + systeme.Configuration.nodeVisited + " nœuds\n"
				+ "Nœuds matched : " + systeme.Configuration.nodeMatched.size() + " nœuds\n"
				//+ "Trouvé : " + (resultat == null ? 0 : ((BF) resultat).size()) + " filtres\n\n";
				+ "Trouvé : " + (resultat == null ? 0 : ((ArrayList<BF>) resultat).size()) + "\n\n";

				
		wf.write(s);
		wf.write(systeme.Configuration.nodeMatched + "\n\n");
		//wf.write(((resultat == null) ? "0" : ((BF) resultat).toString()));
		wf.write(((resultat == null) ? "0" : ((ArrayList<BF>) resultat).toString()));
		wf.close();
		
		System.out.println("Finish");
		
	}
}
