package peerSimTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import peersim.core.Network;
import serveur.NameToID;

public class Config {

	private ArrayList<String> nodeMatched = new ArrayList<String>();
	private Hashtable<Integer, String> indexHeight = new Hashtable<Integer, String>();
	private Hashtable<Integer, Object> listAnswers = new Hashtable<Integer, Object>();
	private int[] nodePerServer = new int[Network.size()];
	private boolean[] peerCreated = new boolean[Network.size()];
	private Hashtable<Integer, Long> timeGlobal = new Hashtable<Integer, Long>();
	
	public static int indexRand = 99999999;
	public static int doublon = 0;
	public static int numberOfBF = 0;
	public static int sizeOfBF = 512;
	public static int numberOfFragment = 64;
	public static int gamma = 1000;
	private NameToID translate = new NameToID(0);
	private boolean end_OK = false;
	public static boolean ObserverNw_OK = false;

	private int nodeVisited = 0;
	private int totalFilterCreated = 0;
	private int totalFilterAdded = 0;
	private int nodeCreated = 0;
	private long time = 0;
	private int numberOfFilter = 0;
	
	private boolean experience_OK = true;
	
	public static String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
	public static String peerSimLOG = "/Users/dcs/vrac/test/"+ date + "_log";
	public static String peerSimLOG_resultat = "/Users/dcs/vrac/test/" + date+ "_resultat_log";
	public static String peerSimLOG_path = "/Users/dcs/vrac/test/" + date + "_path_log";
	
	public Config()
	{
		for (int i = 0; i < Network.size(); i++)
		{
			peerCreated[i] = false;
			nodePerServer[i] = 0;
		}
		
		nodeVisited = 0;
		nodeMatched = new ArrayList<String>();
		indexHeight = new Hashtable<Integer, String>();
		listAnswers = new Hashtable<Integer, Object>();
		doublon = 0;
		numberOfBF = 0;
		date = (new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss")).format(new Date());
		time = 0;
		totalFilterCreated = 0;
		nodeCreated = 0;
		nodePerServer = new int[Network.size()];
		
		for (int i = 0; i < Network.size(); i++)
			nodePerServer[i] = 0;
		
		numberOfFilter = 0;
	}
	
	public synchronized void addNodeCreated(int i)
	{
		nodeCreated += i;
	}
	
	public int getNodeCreated()
	{
		return this.nodeCreated;
	}
	
	public synchronized void addTotalFilterCreated(int i)
	{
		this.totalFilterCreated += i;
	}
	
	public int getTotalFilterCreated()
	{
		return this.totalFilterCreated;
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
	
	public synchronized void setNodePerServer(int index, int i)
	{
		this.nodePerServer[index] = i;
	}
	
	public int getNodePerServer(int index)
	{
		return this.nodePerServer[index];
	}
	
	public synchronized void setPeerCreated(int index, boolean value)
	{
		this.peerCreated[index] =  value;
	}
	
	public boolean getPeerCreated(int index)
	{
		return this.peerCreated[index];
	}
	
	public Hashtable<Integer, String> getIndexHeight()
	{
		return this.indexHeight;
	}
	
	public synchronized void setExperience_OK(boolean val)
	{
		this.experience_OK = val;
	}
	
	public boolean getExperience_OK()
	{
		return this.experience_OK;
	}
	
	public Hashtable<Integer, Long> getTimeGlobal()
	{
		return this.timeGlobal;
	}
	
	public synchronized void setEnd_OK(boolean val)
	{
		this.end_OK = val;
	}
	
	public boolean getEnd_OK()
	{
		return this.end_OK;
	}
}
