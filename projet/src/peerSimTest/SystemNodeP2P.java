package peerSimTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class SystemNodeP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int server;
	private String path;
	private int rang;
	private LocalRouteP2P localRoute;
	private int limit;
	
	public SystemNodeP2P(int server, String path, int rang, int limit) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.path = path;
		this.limit = limit;
		this.rang = rang;
		localRoute = new LocalRouteP2P(limit);
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
	
	public LocalRouteP2P getLocalRoute()
	{
		return this.localRoute;
	}
	
	public int getLimit()
	{
		return this.limit;
	}
	
	public Object add(BFP2P bf)
	{	
		FragmentP2P f = bf.getFragment(rang);
		
		if(localRoute.add(f, bf))
		{
			return null;
		}

		return localRoute.get(f);
	}
	
	public void add(BFP2P bf, String path)
	{
		localRoute.add(bf.getFragment(rang), path);
	}
	
	public Object search(BFP2P bf)
	{
		FragmentP2P f = bf.getFragment(rang);
		ArrayList<Object> rep = new ArrayList<Object>();
		Enumeration<Integer> list = localRoute.getKeyAll();
		
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		/*
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
			*/
		
		while (list.hasMoreElements())
		{
			Integer i = list.nextElement();
			FragmentP2P f_tmp = (new FragmentP2P(0)).intToFragment(bf.getBitsPerElement(), i);
			
			if (f.in(f_tmp))
			{	
				Object bf_tmp = localRoute.get(i);
				
				if (((bf_tmp.getClass()).getName()).equals("java.lang.String"))
				{
					rep.add(bf_tmp);
				}
				else
				{
					ContainerLocalP2P c = (ContainerLocalP2P) bf_tmp;
					Iterator<BFP2P> iterator = c.iterator();
					
				//	boolean ok = true;
					while (iterator.hasNext())
					{
						BFP2P tmp = iterator.next();
						
						if (bf.in(tmp))
						{
							rep.add(tmp);
							ControlerNw.search_log.get(key).addNodeMatched(this.path);
						}
					}
				}
			}
		}
		
		return rep;
	}
	
	public Object searchExact(BFP2P bf)
	{
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		
		FragmentP2P f = bf.getFragment(rang);
		if (localRoute.contains(f))
		{
			Object o = localRoute.get(f);
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				return (String)o;
			}else{
				if (((ContainerLocalP2P)o).contains(bf))
				{
					ControlerNw.search_log.get(key).addNodeMatched(this.path);
					return ((ContainerLocalP2P)o);
				}
				return null;
			}
		}
		return null;
	}
	
	public Object remove(BFP2P bf)
	{
		FragmentP2P f = bf.getFragment(rang);
		if (!localRoute.contains(f))
			return null;
		
		if ((((localRoute.get(f)).getClass()).getName()).equals("peerSimTest.ContainerLocalP2P"))
		{
			ContainerLocalP2P c = (ContainerLocalP2P) localRoute.get(f);
			c.remove(bf);
			
			if(c.isEmpty())
				localRoute.remove(f);
			
			if (localRoute.isEmpty())
				return localRoute;
			return null;
		}
		return localRoute.get(f);
	}
	
	public boolean remove(FragmentP2P f)
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
	
}







