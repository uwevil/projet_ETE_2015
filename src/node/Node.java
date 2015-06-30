package node;

import java.net.Socket;

import simulateur.Simulateur;

public class Node implements Runnable{
	private char[] ID;
	private int level = 0;
	private boolean feuille = true;
	private ConnectRoot connectRoot = new ConnectRoot();
	private int IP = -1;
	private int root = Simulateur.ROOT;
	private int father = -1;
	private VA va = new VA();
	
	public Node(char[] ID, int father, int level) {
		// TODO Auto-generated constructor stub
		this.ID = ID;
		this.father = father;
		this.level = level;
	}
	
	public void setID(char[] ID)
	{
		this.ID = ID;
	}

	public void setIP(int IP)
	{
		this.IP = IP;
	}
	
	public void setFather(int father)
	{
		this.father = father;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public void setLimit(int limit)
	{
		this.va.setLimit(limit);
	}
	
	public char[] getID()
	{
		return this.ID;
	}
	
	public int getIP()
	{
		return this.IP;
	}
	
	public int getFather()
	{
		return this.father;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public void setRoot(int root)
	{
		this.root = root;
	}
	
	public void split()
	{
		
	}
	
	public void add(Filter filter)
	{
		Filter tmp = new Filter(ID);
		Filter tmp2 = filter.createIndex(level);
		if (!tmp2.in(tmp))
			return;
		if (va.add(new Couple(filter)))
			return;
		this.split();
	}
	
	public void run()
	{
		
	}

}
