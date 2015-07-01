package node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import simulateur.Simulateur;

public class VA implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int limit = Simulateur.LIMIT;
	ArrayList<Couple> list = new ArrayList<Couple>(); // plus rapid si accès par indice
	//LinkedList<Couple> list = new LinkedList<Couple>(); // plus lent si accès par indice
	
	public VA()
	{
	}
	
	public VA(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
	}

	public int getSize()
	{
		return list.size();
	}
	
	public int getLimit()
	{
		return this.limit;
	}
	
	public void setLimit(int limit)
	{
		this.limit = limit;
	}
	
	public boolean add(Couple couple)
	{
		if (this.list.size() == this.limit)
			return false;
		
		list.add(couple);
		return true;
	}
	
}
