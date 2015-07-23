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
import test.WriteFile;

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
			while (iterator.hasNext())
			{
				bf = iterator.next();
				this.add(bf, path);	
			}
			this.listNode.put(path, n);
			return null;
		}
		else
		{ // rep to noeud local : creer systemNode, path, rang, containerlocal
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
		//*******LOG*******
		WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG+"_cerateNode", true);
		wf.write("createNode of "+ indexName + " node "+ serverID + "\n"
				+ "Path : " + path + "\n"
				+ "\n");
		wf.close();
		//*****************
	}
	
	@SuppressWarnings("unchecked")
	
	
	// search RETURN tableau 
	// 0 : ArrayList<BF>
	// 1 : Hashtable<Integer, ArrayList<String>>
	
	public Object search(BF bf, String path)
	{
		SystemNode n = (SystemNode)listNode.get(path);
		
		if (n == null)
			return null;
		
		systeme.Configuration.nodeVisited++;

		Object[] resultat = new Object[2];
		resultat[0] = new ArrayList<BF>();
		resultat[1] = new Hashtable<Integer, ArrayList<String>>();
		
		ArrayList<Object> list = (ArrayList<Object>) n.search(bf);
		
		int i = 0;
		while (i < list.size())
		{
			Object o = list.get(i);
			
			if (((o.getClass()).getName()).equals("systeme.BF"))
			{
				((ArrayList<BF>) resultat[0]).add((BF)o);
			}
			else
			{
				if (!this.listNode.containsKey((String)o))
				{
					systeme.Configuration.translate.setLength(Network.size());
					int serverID_tmp = systeme.Configuration.translate.translate((String)o);
					
					if (((Hashtable<Integer, ArrayList<String>>) resultat[1]).containsKey(serverID_tmp))
					{
						ArrayList<String> als = (((Hashtable<Integer, ArrayList<String>>) resultat[1]).get(serverID_tmp));
						if (!als.contains((String)o))
								als.add((String)o);
					}
					else // not contains serverID_tmp
					{
						ArrayList<String> al = new ArrayList<String>();
						al.add((String)o);
						((Hashtable<Integer, ArrayList<String>>) resultat[1]).put(serverID_tmp, al);
					}
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
