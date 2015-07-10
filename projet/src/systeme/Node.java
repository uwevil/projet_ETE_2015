package systeme;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import serveur.Server;

public class Node {
	private Server server;
	private String path;
	private int rang;
	private LocalRoute localRoute;
	private int limit;
	
	public Node(Server server, String path, int rang, int limit) {
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
	
	public Server getServer()
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
					test.TestSystemIndex.nodeMatched++;
				}else{
					ContainerLocal c = (ContainerLocal) bf_tmp;
					Iterator<BF> iterator = c.iterator();
					
					while (iterator.hasNext())
					{
						BF tmp = iterator.next();
						
						if (bf.in(tmp))
							rep.add(tmp);
					}
				}

			}
		}
			
		return rep;
	}
	
	public Object searchExact(BF bf)
	{
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

	public String toString()
	{
		return "NodeID : " + path + "\n" + localRoute.toString();
	}
	
	public String overView()
	{
		return "NodeID : " + path + "\n" + localRoute.overView();
	}
	
}







