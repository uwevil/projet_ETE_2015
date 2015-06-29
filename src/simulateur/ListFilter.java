package simulateur;

import java.util.ArrayList;
import java.util.Iterator;

import node.Filter;

public class ListFilter {
	private ArrayList<Filter> list;
	
	public ListFilter() {
		list = new ArrayList<Filter>();
	}
	
	public int size()
	{
		return list.size();
	}
	
	public void put(Filter f)
	{
		if (list.contains(f))
			return;
		list.add(f);
	}

	public Filter get(int index)
	{
		return list.get(index);
	}
	
	public Filter remove(int index)
	{
		Filter tmp = list.get(index);
		list.remove(index);
		return tmp;
	}
	
	public Iterator<Filter> iterator()
	{
		return list.iterator();
	}
	
}
