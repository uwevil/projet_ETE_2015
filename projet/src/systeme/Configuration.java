package systeme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import peersim.core.Network;
import serveur.NameToID;

public class Configuration {
	public static int nodeVisited = 0;
	public static ArrayList<String> nodeMatched = new ArrayList<String>();
	public static Hashtable<Integer, ArrayList<String>> graph = new Hashtable<Integer, ArrayList<String>>();
	public static Hashtable<Integer, ArrayList<String>> dispo = new Hashtable<Integer, ArrayList<String>>();
	public static Hashtable<Integer, String> indexHeight = new Hashtable<Integer, String>();
	public static int filterPerNode = 0;
	public static int[] nodePerServer = new int[Network.size()];
	
	public static int indexRand = 99999999;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	public static int sizeOfBF = 512;
	public static int numberOfFragment = 64;
	public static int gamma = 1000;
	public static NameToID translate = new NameToID(0);
	public static int totalFilterAdded = 0;
	public static int nodeCreated = 0;
	
	public static String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
	public static String peerSimLOG = "/Users/dcs/vrac/test/"+ date + "_log";
	public static String peerSimLOG_resultat = "/Users/dcs/vrac/test/" + date+ "_resultat_log";
	public static String peerSimLOG_path = "/Users/dcs/vrac/test/" + date + "_path_log";
	public static long time = 0;
	public static int numberOfFilter = 0;

	
	public Configuration()
	{
		nodeVisited = 0;
		nodeMatched = new ArrayList<String>();
		graph = new Hashtable<Integer, ArrayList<String>>();
		dispo = new Hashtable<Integer, ArrayList<String>>();
		indexHeight = new Hashtable<Integer, String>();
		doublon = 0;
		numberOfBF = 0;
		date = (new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss")).format(new Date());
		time = 0;
		numberOfFilter = 0;
		totalFilterAdded = 0;
		nodeCreated = 0;
		filterPerNode = 0;
		nodePerServer = new int[Network.size()];
	}
}
