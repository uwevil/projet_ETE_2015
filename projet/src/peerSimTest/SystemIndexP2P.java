package peerSimTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import peersim.core.Network;
import serveur.Message;
import systeme.BF;
import systeme.CalculRang;
import systeme.ContainerLocal;
import systeme.Fragment;
import systeme.SystemNode;

public class SystemIndexP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String indexName;
	private int serverID;
	private int gamma;
	private Hashtable<String, SystemNode> listNode;
	
	public SystemIndexP2P(String indexName, int serverID, int gamma) {
		// TODO Auto-generated constructor stub
		this.indexName = indexName;
		this.serverID = serverID;
		this.gamma = gamma;
		listNode = new Hashtable<String, SystemNode>();
	}
	
	public String getIndexName()
	{
		return this.indexName;
	}
	
	public void createRoot()
	{
		listNode.put("", new SystemNode(serverID, "", 0, gamma));
	}
	
	public Object add(BF bf, String path)
	{
		SystemNode n =  (SystemNode)listNode.get(path);
		
		if (n == null)
		{
			n = new SystemNode(serverID, path, (new CalculRang()).getRang(path), gamma);
			n.add(bf);
			return null;
		}
		else
		{
			Object o = n.add(bf);
			
			if (o == null)
				return null;
			
			while (o != null)
			{
				if (((o.getClass()).getName()).equals("systeme.ContainerLocal"))
				{				
					o = this.split(n, (ContainerLocal)o);
					return o;
				} 
				else if (((o.getClass()).getName()).equals("java.lang.String"))
				{
					if (listNode.containsKey(o))
					{
						n = (SystemNode)listNode.get(o);
						o = n.add(bf);
					}
					else
					{
						return o;
					}
				}
			}
		}
		return null;
	}

	private Object split(SystemNode father, ContainerLocal c)
	{
		Iterator<BF> iterator = c.iterator();
		BF bf = c.get(0);
		Fragment f = bf.getFragment(father.getRang());
		String path = father.getPath() + "/" + f.toInt();
		
		Message rep = new Message();
		systeme.Configuration.translate.setLength(Network.size());
		int tmp_serverID = systeme.Configuration.translate.translate(path);
		
		father.add(bf,path);
		
		if (tmp_serverID == serverID)
		{
			SystemNode n = new SystemNode(serverID, path, father.getRang() + 1, gamma);
			this.listNode.put(path, n);
			while (iterator.hasNext())
			{
				bf = iterator.next();
				n.add(bf);	
			}
			return null;
		}else{ // rep to noeud local : creer systemNode, path, rang, containerlocal
			rep.setIndexName(indexName);
			rep.setData(c);
			rep.setPath(path);
		}
		
		return rep;
	}
	
	public void addSystemNode(String path, SystemNode node)
	{
		if (!this.listNode.containsKey(path))
			this.listNode.put(path, node);
	}
	
	@SuppressWarnings("unchecked")
	public Object search(BF bf, String path)
	{
		SystemNode n = (SystemNode)listNode.get(path);
		
		if (n == null)
			return null;
		
		systeme.Configuration.nodeVisited++;

		ArrayList<Object> resultat = new ArrayList<Object>();
		ArrayList<Object> list = (ArrayList<Object>) n.search(bf);
		
		int i = 0;
		while (i < list.size())
		{
			Object o = list.get(i);
			
			if (((o.getClass()).getName()).equals("systeme.BF"))
			{
				resultat.add((BF)o);
			}else{
				if (!this.listNode.containsKey((String)o))
				{
					resultat.add(o);
				}
				else
				{
					SystemNode node_tmp = (SystemNode)listNode.get((String)o);
					systeme.Configuration.nodeVisited++;
					list.addAll((ArrayList<Object>) node_tmp.search(bf));
				}
			}
			i++;
		}
		return resultat;
	}
	 
	
	public Object searchExact(BF bf, String path)
	{
		SystemNode n = (SystemNode)listNode.get(path);
		
		if (n == null)
			return null;
		
		systeme.Configuration.nodeMatched.add(path);
		Object o = n.searchExact(bf);
		
		while(o != null)
		{
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				if (!listNode.containsKey((String)o))
				{
					return o;
				}
				else
				{
					n = listNode.get((String)o);
					systeme.Configuration.nodeVisited++;
					systeme.Configuration.nodeMatched.add((String)o);
					o = n.searchExact(bf);
				}
			}else{
				Iterator<BF> iterator = ((ContainerLocal)o).iterator();
				
				while (iterator.hasNext())
				{
					BF tmp = iterator.next();
					if (bf.equals(tmp))
						return tmp;
				}
			}
		}
		return null;
	}
	
	public Object remove(BF bf, String path)
	{
		SystemNode n = (SystemNode)listNode.get(path);
		if (n == null)
			return null;
		
		Object o = n.remove(bf);
		if (o == null)
			return null;
		if (path == "")
			return null;
		
		while (o != null)
		{
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				if (!listNode.containsKey((String)o))
				{
					return o;
				}
				else
				{
					n = listNode.get((String)o);
					o = n.remove(bf);
				}
			}else{ // localRoute
				String path_tmp = n.getPath();
				int rang_tmp = n.getRang();

				if (path_tmp == "")
					return null;
				
				listNode.remove(path_tmp);

				int endIndex = path_tmp.lastIndexOf('/');
				
				if (!this.listNode.containsKey(path_tmp.substring(0, endIndex)))
					return path_tmp.substring(0, endIndex);
				
				n = (SystemNode)listNode.get(path_tmp.substring(0, endIndex));
				
				while(true)
				{
					if (n.remove(bf.getFragment(rang_tmp)))
					{
						return null;
					}
					else
					{
						path_tmp = n.getPath();
						rang_tmp = n.getRang();

						if (path_tmp == "")
							return null;
						
						listNode.remove(path);

						endIndex = path.lastIndexOf('/');
						if (!this.listNode.containsKey(path_tmp.substring(0, endIndex)))
							return path_tmp.substring(0, endIndex);
						
						n = (SystemNode)listNode.get(path.substring(0, endIndex));
					}
				}
			}
		}
		return null;
	}
	
	public int size()
	{
		return this.listNode.size();
	}
	
	public String toString()
	{
		String s = new String();
		
		Enumeration<SystemNode> e = listNode.elements();
		
		while(e.hasMoreElements())
			s += (e.nextElement()).toString() + "\n";
		
		return s;
	}
	
	public String overView()
	{
		String s = new String();
		
		Enumeration<SystemNode> e = listNode.elements();
		
		while(e.hasMoreElements())
			s += (e.nextElement()).overView() + "\n";
		
		return s;
	}
}
