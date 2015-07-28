package peerSimTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ContainerLocalP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int limit;
	private ArrayList<BFP2P> container;
	
	public ContainerLocalP2P(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		container = new ArrayList<BFP2P>(limit);
	}
	
	public int limit()
	{
		return this.limit;
	}
	
	public int getNumberOfElements()
	{
		return this.container.size();
	}
	
	public boolean add(BFP2P e)
	{
		if (this.container.size() == this.limit)
		{
			container.add(e);
			ControlerNw.config_log.addTotalFilterAdded(-this.limit);
			return false;
		}
			
		if (!this.container.contains(e))
		{
			container.add(e);
			ControlerNw.config_log.addTotalFilterAdded(1);
			return true;
		}
		return true;
	}
	
	public boolean isEmpty()
	{
		return this.container.isEmpty();
	}
	
	public BFP2P get(int index)
	{
		if (index >= this.limit)
			return null;
		
		try
		{
			container.get(index);
		} catch (Exception e){
			return null;
		}
		
		return container.get(index);
	}
	
	public void remove(BFP2P e)
	{
		if (!container.contains(e))
			return;
		container.remove(e);
	}
	
	public boolean contains(BFP2P e)
	{
		return container.contains(e);
	}
	
	public boolean contains(int index)
	{
		try
		{
			container.get(index);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public String toString()
	{
		String s = new String();
		Iterator<BFP2P> iterator = container.iterator();
		while(iterator.hasNext())
		{
			BFP2P bf = iterator.next();
			s += "   " +bf + "\n";
			/*if (iterator.hasNext())
				s += ",";
			*/
		}

		return s;
	}
	
	public Iterator<BFP2P> iterator()
	{
		return container.iterator();
	}

}
