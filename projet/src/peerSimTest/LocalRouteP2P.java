package peerSimTest;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class LocalRouteP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* LocalRoute contient soit un Container soit un String 
	 * */
	private int limit;
	private Hashtable<Integer, Object> localRoute;
	
	public LocalRouteP2P(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		localRoute = new Hashtable<Integer, Object>();
	}
	
	public boolean add(FragmentP2P f, BFP2P bf)
	{	
		
		if (!this.contains(f))
		{
			ContainerLocalP2P c = new ContainerLocalP2P(limit);
			c.add(bf);
			localRoute.put(f.toInt(), c);
			return true;
		}else{
			if ((((this.get(f)).getClass()).getName()).equals("java.lang.String"))
			{
				return false;
			}else{
				return ((ContainerLocalP2P) this.get(f)).add(bf);
			}
		}
	}
	
	public void add(FragmentP2P f, String path)
	{
		if (!this.contains(f))
		{
			localRoute.put(f.toInt(), path);
		}else{
			localRoute.remove(f.toInt());
			localRoute.put(f.toInt(), path);
		}
	}
	
	public Object get(FragmentP2P f)
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
	
	public boolean contains(FragmentP2P f)
	{
		return localRoute.containsKey(f.toInt());
	}
	
	public boolean contains(int index)
	{
		return localRoute.containsKey(index);
	}
	
	public void remove(FragmentP2P f)
	{
		this.localRoute.remove(f.toInt());
	}
	
	public void remove(int index)
	{
		this.localRoute.remove(index);
	}
	
	public Enumeration<Integer> getKeyAll()
	{
		return localRoute.keys();
	}
	
	public String toString()
	{
		String s = new String();
		
		Enumeration<Integer> e = localRoute.keys();
		
		while(e.hasMoreElements())
		{
			Integer o = e.nextElement();
			s += "  ContainerLocalP2P n° " + o.toString() + " : " + localRoute.get(o).toString() + "\n";
		}		
		return s;
	}
	
	public boolean isEmpty()
	{
		return localRoute.isEmpty();
	}

}
