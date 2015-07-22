package systeme;

import java.util.ArrayList;
import java.util.Hashtable;

import serveur.NameToID;

public class Configuration {
	public static int nodeVisited = 0;
	public static ArrayList<String> nodeMatched = new ArrayList<String>();
	public static Hashtable<Integer, ArrayList<String>> graph = new Hashtable<Integer, ArrayList<String>>();
	public static Hashtable<Integer, ArrayList<String>> dispo = new Hashtable<Integer, ArrayList<String>>();
	
	public static int indexRand = 99999999;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	public static int sizeOfBF = 512;
	public static int numberOfFragment = 64;
	public static int gamma = 1000;
	public static NameToID translate = new NameToID(0);
	
	public static String peerSimLOG = "/Users/dcs/vrac/test/22-07-peersim_log";
	public static String peerSimLOG_resultat = "/Users/dcs/vrac/test/22-07-peersim_resultat_log";
	public static int numberOfTime = 0;

	
	public Configuration()
	{
		nodeVisited = 0;
		nodeMatched = new ArrayList<String>();
		graph = new Hashtable<Integer, ArrayList<String>>();
		doublon = 0;
		numberOfBF = 0;
	}
}
