package systeme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import serveur.Server;

public class SystemIndex implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int indexID;
	@SuppressWarnings("unused")
	private Server serverID;
	private int gamma;
	private Hashtable<String, Node> listNode;
	
	public SystemIndex(int indexID, Server serverID, int gamma) {
		// TODO Auto-generated constructor stub
		this.indexID = indexID;
		this.serverID = serverID;
		this.gamma = gamma;
		listNode = new Hashtable<String, Node>();
		listNode.put("", new Node(null, "", 0, gamma));
	}
	
	public int getIndexID()
	{
		return this.indexID;
	}

	public void add(BF bf)
	{
		Node n =  (Node)listNode.get("");
		
		Object o = n.add(bf);
		if (o == null)
			return;
		
		while (o != null)
		{
			if (((o.getClass()).getName()).equals("systeme.ContainerLocal"))
			{				
				this.split(n, (ContainerLocal)o);
				o = null;
			} else if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				n = (Node)listNode.get(o);
				o = n.add(bf);
			}
		}
	}
	
	private void split(Node father, ContainerLocal c)
	{
		Iterator<BF> iterator = c.iterator();
		BF bf = c.get(0);
		Fragment f = bf.getFragment(father.getRang());
		String path = father.getPath() + "/" + f.toInt();
		Node n = new Node(null, path, father.getRang() + 1, gamma);
		father.add(bf,path);
		this.listNode.put(path, n);
		
		while (iterator.hasNext())
		{
			bf = iterator.next();
			n.add(bf);	
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object search(BF bf)
	{
		@SuppressWarnings("unused")
		Configuration c = new Configuration();
		Node n = (Node)listNode.get("");
		ArrayList<BF> resultat = new ArrayList<BF>();
		ArrayList<Object> list = (ArrayList<Object>) n.search(bf);
		systeme.Configuration.nodeVisited++;
		
		int i = 0;
		while (i < list.size())
		{
			Object o = list.get(i);
			
			if (((o.getClass()).getName()).equals("systeme.BF"))
			{
				resultat.add((BF)o);
			}else{
				Node node_tmp = (Node)listNode.get((String)o);
				systeme.Configuration.nodeVisited++;
				list.addAll((ArrayList<Object>) node_tmp.search(bf));
			}
			i++;
		}
		return resultat;
	}
	 
	
	public Object searchExact(BF bf)
	{
		@SuppressWarnings("unused")
		Configuration c = new Configuration();
		Node n = (Node)listNode.get("");
		Object o = n.searchExact(bf);
		
		while(o != null)
		{
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				n = listNode.get((String)o);
				systeme.Configuration.nodeVisited++;
				o = n.searchExact(bf);
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
	
	public void remove(BF bf)
	{
		Node n = (Node)listNode.get("");
		Object o = n.remove(bf);
		
		while (o != null)
		{
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				n = listNode.get((String)o);
				o = n.remove(bf);
			}else{
				String path = n.getPath();
				int rang = n.getRang();

				if (path == "")
					return;
				
				listNode.remove(path);

				int lastIndex = path.lastIndexOf('/');
				n = (Node)listNode.get(path.substring(0, lastIndex));
				while(true)
				{
					if (n.remove(bf.getFragment(rang)))
					{
						return;
					}else{
						path = n.getPath();
						rang = n.getRang();

						if (path == "")
							return;
						
						listNode.remove(path);

						lastIndex = path.lastIndexOf('/');
						n = (Node)listNode.get(path.substring(0, lastIndex));
					}
				}
			}
		}
	}
	
	public int size()
	{
		return this.listNode.size();
	}
	
	public String toString()
	{
		String s = new String();
		
		Enumeration<Node> e = listNode.elements();
		
		while(e.hasMoreElements())
			s += (e.nextElement()).toString() + "\n";
		
		return s;
	}
	
	public String overView()
	{
		String s = new String();
		
		Enumeration<Node> e = listNode.elements();
		
		while(e.hasMoreElements())
			s += (e.nextElement()).overView() + "\n";
		
		return s;
	}
}
