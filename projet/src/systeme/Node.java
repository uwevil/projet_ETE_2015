package systeme;

import java.util.ArrayList;
import java.util.Iterator;

import serveur.Message;
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
	
	public Object treatMessage(Message m)
	{
		switch((m.getType()).toLowerCase())
		{
		case "add":
			return this.add(m);
		case "search":
			return this.search(m);
		case "remove":
			return this.remove(m);
		}
		
		return null;
	}
	
	private Message add(Message m)
	{
		Message rep = new Message();
		BF bf = (BF) m.getData1();
		Fragment f = bf.getFragment(this.rang + 1);
		
		if (!this.localRoute.add(f, bf))
		{
			if (((ContainerLocal)this.localRoute.get(f)).getNumberOfElements() == this.limit)
			{
				/*
				 * SPLIT()
				 * */
				rep.setType("createNode");
				rep.setData1(f);
				rep.setData2(this.rang);
				rep.setData3((ContainerLocal)this.localRoute.get(f));
				rep =  server.treatMessage(rep);
				this.localRoute.add(f, path+"/"+f.toInt());
			}else{
				/* 
				 * server.forward(message)
				 * */
				rep.setType("forward");
				rep.setData1(bf);
				rep.setData2((String)this.localRoute.get(f));
				rep = server.treatMessage(rep);
			}
		}
		
		return rep;
	}
	
	@SuppressWarnings("unchecked")
	private Message search(Message m)
	{
		Message rep = new Message();
		BF bf = (BF)m.getData1();
		Fragment f = bf.getFragment(this.rang + 1);
		
		ArrayList<Fragment> listFragment = new ArrayList<Fragment>();
		
		listFragment.add(0, f);
		int i = 0, j = 0, k =0;
		
		Fragment tmp1 = f;
		for(i = 0; i < f.size(); i++)
		{
			for (j = i; j < f.size(); j++)
			{
				if (!tmp1.get(j))
				{
					Fragment tmp = tmp1;
					tmp.setBit(j, true);
					listFragment.add(k++, tmp);
				}
			}
			tmp1.setBit(i, true);
		}
		
		ArrayList<Object> listReponse = new ArrayList<Object>();
		for (Fragment tmp : listFragment)
		{
			/*teste dans localRoute
			 * 
			 * */
			if (localRoute.contains(tmp))
			{
				Object o = localRoute.get(tmp);
				switch(o.getClass().getName())
				{
				case "systeme.ContainerLocal":
					ContainerLocal c = (ContainerLocal)o;
					Iterator<BF> iterator = c.iterator();
					while(iterator.hasNext())
					{
						BF bf_tmp = iterator.next();
						if (bf.in(bf_tmp))
							listReponse.add(bf_tmp);
					}
				case "java.lang.String":
					rep = server.treatMessage(m);
					
					listReponse.addAll((ArrayList<Object>) rep.getData2());
				}
			}
			
		}
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * besoin de changer type de message
		 * 
		 * 
		 * 
		 * 
		 * */
		rep.setType("OK");
		rep.setData1(bf);
		rep.setData2(listReponse);
		return rep;
	}
	
	private Message remove(Message m)
	{
		Message rep = new Message();
		BF bf = (BF) m.getData1();
		Fragment f = bf.getFragment(this.rang + 1);
		
		if (!localRoute.contains(f))
		{
			rep.setType("OK");
			rep.setData1(bf);
			return rep;
		}
		
		Object o = localRoute.get(f);
		switch(o.getClass().getName())
		{
		case "systeme.ContainerLocal":
			localRoute.remove(f);
			if (localRoute.isEmpty())
			{
				rep.setType("removeNode");
				rep.setData1(path);
				server.treatMessage(rep);
				return null;
			}
		case "java.lang.String":
			rep = server.treatMessage(m);	
			if (rep.getType().equals("removeNode"))
				localRoute.remove(f);
			if (localRoute.isEmpty())
			{
				rep.setType("removeNode");
				rep.setData1(path);
				server.treatMessage(rep);
				return null;
			}	
		}
		return rep;
	}
	
	public LocalRoute getLocalRoute()
	{
		return this.localRoute;
	}
	
	public int getLimit()
	{
		return this.limit;
	}

}
