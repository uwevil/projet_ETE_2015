package peerSimTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import peersim.core.Network;
import serveur.NameToID;

public class Config {

	private ArrayList<String> nodeMatched = new ArrayList<String>();
	public static Hashtable<Integer, String> indexHeight = new Hashtable<Integer, String>();
	private Hashtable<Integer, Object> listAnswers = new Hashtable<Integer, Object>();
	private int[] nodePerServer = new int[Network.size()];
	
	public static int indexRand = 99999999;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	public static int sizeOfBF = 512;
	public static int numberOfFragment = 64;
	public static int gamma = 1000;
	private NameToID translate = new NameToID(0);

	private int nodeVisited = 0;
	private int totalFilterAdded = 0;
	private int nodeCreated = 0;
	private long time = 0;
	private int numberOfFilter = 0;
	private int filterPerNode = 0;
	public static int nodeTotal = 0;
	
	public static String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
	public static String peerSimLOG = "/Users/dcs/vrac/test/"+ date + "_log";
	public static String peerSimLOG_resultat = "/Users/dcs/vrac/test/" + date+ "_resultat_log";
	public static String peerSimLOG_path = "/Users/dcs/vrac/test/" + date + "_path_log";
	
	public Config()
	{
		nodeVisited = 0;
		nodeMatched = new ArrayList<String>();
		indexHeight = new Hashtable<Integer, String>();
		listAnswers = new Hashtable<Integer, Object>();
		doublon = 0;
		numberOfBF = 0;
		date = (new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss")).format(new Date());
		time = 0;
		totalFilterAdded = 0;
		nodeCreated = 0;
		filterPerNode = 0;
		nodePerServer = new int[Network.size()];
		
		for (int i = 0; i < Network.size(); i++)
			nodePerServer[i] = 0;
		
		numberOfFilter = 0;
		nodeTotal = 0;
	}
	
	public synchronized void addNodeCreated(int i)
	{
		nodeCreated += i;
	}
	
	public int getNodeCreated()
	{
		return this.nodeCreated;
	}
	
	public synchronized void addTotalFilterAdded(int i)
	{
		this.totalFilterAdded += i;
	}
	
	public int getTotalFilterAdded()
	{
		return this.totalFilterAdded;
	}
	
	public synchronized void addNumberOfFilters(int i)
	{
		this.numberOfFilter += i;
	}
	
	public int getNumberOfFilters()
	{
		return this.numberOfFilter;
	}
	
	public synchronized void setTime(long time)
	{
		this.time = time;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public synchronized void addNodeVisited(int i)
	{
		this.nodeVisited += i;
	}
	
	public int getNodeVisited()
	{
		return this.nodeVisited;
	}
	
	public void putListAnswer(Integer key, Object value)
	{
		this.listAnswers.put(key, value);
	}
	
	public Object getListAnswer(Integer key)
	{
		return this.listAnswers.get(key);
	}
	
	public boolean containsKeyListAnswer(Integer key)
	{
		return this.listAnswers.containsKey(key);
	}
	
	public void removeListAnswer(Integer key)
	{
		this.listAnswers.remove(key);
	}
	
	public void addNodeMatched(String s)
	{
		if (this.nodeMatched.contains(s))
			return;
		
		this.nodeMatched.add(s);
	}
	
	public ArrayList<String> getNodeMatched()
	{
		return this.nodeMatched;
	}
	
	public int sizeNodeMatched()
	{
		return this.nodeMatched.size();
	}
	
	public NameToID getTranslate()
	{
		return this.translate;
	}
	
	public synchronized void addFilterPerNode(int i)
	{
		this.filterPerNode += i;
	}
	
	public int getFilterPerNode()
	{
		return this.filterPerNode;
	}
	
	public synchronized void setNodePerServer(int index, int i)
	{
		this.nodePerServer[index] = i;
	}
	
	public int getNodePerServer(int index)
	{
		return this.nodePerServer[index];
	}
	
}
