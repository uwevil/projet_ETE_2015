package peerSimTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import systeme.BF;
import systeme.ContainerLocal;
import systeme.Fragment;
import systeme.LocalRoute;

public class SystemNodeP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int server;
	private String path;
	private int rang;
	private LocalRoute localRoute;
	private int limit;
	
	public SystemNodeP2P(int server, String path, int rang, int limit) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.path = path;
		this.limit = limit;
		this.rang = rang;
		localRoute = new LocalRoute(limit);
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public int getServer()
	{
		return this.server;
	}
	
	public int getRang()
	{
		return this.rang;
	}
	
	public LocalRoute getLocalRoute()
	{
		return this.localRoute;
	}
	
	public int getLimit()
	{
		return this.limit;
	}
	
	public Object add(BF bf)
	{	
		Fragment f = bf.getFragment(rang);
		
		if(localRoute.add(f, bf))
		{
			return null;
		}

		return localRoute.get(f);
	}
	
	public void add(BF bf, String path)
	{
		localRoute.add(bf.getFragment(rang), path);
	}
	
	public Object search(BF bf)
	{
		Fragment f = bf.getFragment(rang);
		ArrayList<Object> rep = new ArrayList<Object>();
		Enumeration<Integer> list = localRoute.getKeyAll();
		
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		
		if (systeme.Configuration.graph.containsKey(rang))
		{
			((ArrayList<String>)(systeme.Configuration.graph.get(rang))).add(path);
		}
		else
		{
			ArrayList<String> als = new ArrayList<String>();
			als.add(path);
			systeme.Configuration.graph.put(rang, als);
		}
				
		while (list.hasMoreElements())
		{
			Integer i = list.nextElement();
			Fragment f_tmp = (new Fragment(0)).intToFragment(bf.getBitsPerElement(), i);
			
			if (f.in(f_tmp))
			{	
				Object bf_tmp = localRoute.get(i);
				
				if (((bf_tmp.getClass()).getName()).equals("java.lang.String"))
				{
					rep.add(bf_tmp);
				}
				else
				{
					ContainerLocal c = (ContainerLocal) bf_tmp;
					Iterator<BF> iterator = c.iterator();
					
				//	boolean ok = true;
					while (iterator.hasNext())
					{
						BF tmp = iterator.next();
						
						if (bf.in(tmp))
						{
							rep.add(tmp);
							ControlerNw.search_log.get(key).addNodeMatched(this.path);
							/*
							if (ok)
							{
								 if(!systeme.Configuration.nodeMatched.contains(this.path))
								 {
									systeme.Configuration.nodeMatched.add(this.path);
									ok = false;
								 }
								 else
								 {
									ok = false;
								 }
							}
							*/
						}
					}
				}
			}
		}
		
		return rep;
	}
	
	public Object searchExact(BF bf)
	{
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		
		Fragment f = bf.getFragment(rang);
		if (localRoute.contains(f))
		{
			Object o = localRoute.get(f);
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				return (String)o;
			}else{
				if (((ContainerLocal)o).contains(bf))
				{
					ControlerNw.search_log.get(key).addNodeMatched(this.path);
				//	systeme.Configuration.nodeMatched.add(this.path);
					return ((ContainerLocal)o);
				}
				return null;
			}
		}
		return null;
	}
	
	public Object remove(BF bf)
	{
		Fragment f = bf.getFragment(rang);
		if (!localRoute.contains(f))
			return null;
		
		if ((((localRoute.get(f)).getClass()).getName()).equals("systeme.ContainerLocal"))
		{
			ContainerLocal c = (ContainerLocal) localRoute.get(f);
			c.remove(bf);
			
			if(c.isEmpty())
				localRoute.remove(f);
			
			if (localRoute.isEmpty())
				return localRoute;
			return null;
		}
		return localRoute.get(f);
	}
	
	public boolean remove(Fragment f)
	{
		if (!localRoute.contains(f))
			return true;
		
		localRoute.remove(f);
		
		if (localRoute.isEmpty())
			return false;
		return true;
	}

	public String toString()
	{
		return "NodeID : " + path + "\n"
			 + "Rang : " + rang + "\n"
			 + localRoute.toString();
	}
	
	public String overView()
	{
		return "NodeID : " + path + "\n" + localRoute.overView();
	}
	
}







