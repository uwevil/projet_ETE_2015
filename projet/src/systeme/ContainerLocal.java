package systeme;

import java.util.ArrayList;
import java.util.Iterator;

public class ContainerLocal{
	private int limit;
	private ArrayList<BF> container;
	
	public ContainerLocal(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		container = new ArrayList<BF>(limit);
	}
	
	public int limit()
	{
		return this.limit;
	}
	
	public int getNumberOfElements()
	{
		return this.container.size();
	}
	
	public boolean add(BF e)
	{
		if (this.container.size() == this.limit)
		{
			container.add(e);
			return false;
		}
			
		if (!this.container.contains(e))
		{
			container.add(e);
			return true;
		}
		return true;
	}
	
	public boolean isEmpty()
	{
		return this.container.isEmpty();
	}
	
	public BF get(int index)
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
	
	public void remove(BF e)
	{
		if (!container.contains(e))
			return;
		container.remove(e);
	}
	
	public boolean contains(BF e)
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
		Iterator<BF> iterator = container.iterator();
		while(iterator.hasNext())
		{
			BF bf = iterator.next();
			s += "   " +bf + "\n";
			/*if (iterator.hasNext())
				s += ",";
			*/
		}

		return s;
	}
	
	public String overView()
	{
		test.TestSystemIndex.numberOfBF += container.size();
		return container.size() + " BF";
	}
	
	public Iterator<BF> iterator()
	{
		return container.iterator();
	}

}
