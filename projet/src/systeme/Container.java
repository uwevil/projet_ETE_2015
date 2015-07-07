package systeme;

import java.util.ArrayList;

public class Container<E> {
	private int limit;
	private ArrayList<E> container;
	
	public Container(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		container = new ArrayList<E>(limit);
	}
	
	public int limit()
	{
	return this.limit;
	}
	
	public int getNumberOfElements()
	{
		return this.container.size();
	}
	
	public boolean add(E e)
	{
		if (this.container.size() == this.limit)
			return false;
		
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
	

}
