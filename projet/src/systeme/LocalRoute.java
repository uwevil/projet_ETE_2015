package systeme;

import java.util.ArrayList;
import java.util.Iterator;

public class LocalRoute {
	/* LocalRoute contient soit un Container soit un String 
	 * */
	private int limit;
	private ArrayList<Object> localRoute;
	
	public LocalRoute(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		localRoute = new ArrayList<Object>();
	}
	
	public void add(Fragment f, ContainerLocal o)
	{
		if (!this.contains(f))
		{
			localRoute.add(f.toInt(), (Object) o);
		}else{
			localRoute.remove(f.toInt());
			localRoute.add(f.toInt(), o);
		}
	}
	
	public boolean add(Fragment f, BF bf)
	{	
		if (!this.contains(f))
		{
			ContainerLocal c = new ContainerLocal(limit);
			c.add(bf);
			localRoute.add(f.toInt(), c);
			return true;
		}else{
			if (this.get(f).getClass().getName().equals("java.lang.String"))
			{
				return false;
			}else{
				return ((ContainerLocal) this.get(f)).add(bf);
			}
		}
	}
	
	public void add(Fragment f, String path)
	{
		if (!this.contains(f))
		{
			localRoute.add(f.toInt(), path);
		}else{
			localRoute.remove(f.toInt());
			localRoute.add(f.toInt(), path);
		}
	}
	
	public Object get(Fragment f)
	{
		if (this.contains(f))
		{
			return localRoute.get(f.toInt());
		}else{
			return null;
		}
	}
	
	public Object get(int index)
	{
		if (this.contains(index))
		{
			return localRoute.get(index);
		}else{
			return null;
		}
	}
	
	public boolean contains(Fragment f)
	{
		try
		{
			localRoute.get(f.toInt());
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public boolean contains(int index)
	{
		try
		{
			localRoute.get(index);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public void remove(Fragment f)
	{
		this.localRoute.remove(f.toInt());
	}
	
	public void remove(int index)
	{
		this.localRoute.remove(index);
	}
	
	public String toString()
	{
		String s = new String();
		
		Iterator<Object> iterator = localRoute.iterator();
		
		while(iterator.hasNext())
		{
			Object o = iterator.next();
			s += o.toString() + "\n";
		}
		
		return s;
	}

}
