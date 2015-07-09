package systeme;

import java.util.Enumeration;
import java.util.Hashtable;

public class LocalRoute {
	/* LocalRoute contient soit un Container soit un String 
	 * */
	private int limit;
	private Hashtable<Integer, Object> localRoute;
	
	public LocalRoute(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		localRoute = new Hashtable<Integer, Object>();
	}
	
	public void add(Fragment f, ContainerLocal o)
	{
		if (!this.contains(f))
		{
			localRoute.put(f.toInt(), (Object) o);
		}else{
			localRoute.remove(f.toInt());
			localRoute.put(f.toInt(), o);
		}
	}
	
	public boolean add(Fragment f, BF bf)
	{	
		if (!this.contains(f))
		{
			ContainerLocal c = new ContainerLocal(limit);
			c.add(bf);
			localRoute.put(f.toInt(), c);
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
			localRoute.put(f.toInt(), path);
		}else{
			localRoute.remove(f.toInt());
			localRoute.put(f.toInt(), path);
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
			if ( null == localRoute.get(f.toInt()))
				return false;
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
		
		Enumeration<Integer> e = localRoute.keys();
		
		while(e.hasMoreElements())
		{
			Integer o = e.nextElement();
			s += " ContainerLocal n° " + o.toString() + " : " + localRoute.get(o).toString() + "\n";
		}		
		return s;
	}
	
	public String overView()
	{
		String s = new String();
		
		Enumeration<Integer> e = localRoute.keys();
		
		while(e.hasMoreElements())
		{
			Integer i = e.nextElement();
			Object o = localRoute.get(i);
			
			if (o.getClass().getName().equals("systeme.ContainerLocal"))
			{
				s += " ContainerLocal n° " + i.toString() + " : " 
						+ ((ContainerLocal)localRoute.get(i)).overView() + "\n";
			}else{
				s += " ContainerLocal n° " + i.toString() + " : " + localRoute.get(i).toString() + "\n";
			}
			
			
		}		
		return s;
	}
	
	public boolean isEmpty()
	{
		return localRoute.isEmpty();
	}

}
