package systeme;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import test.WriteFile;

public class SystemIndexCentral implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int indexID;
	private int serverID;
	private int gamma;
	private Hashtable<String, SystemNode> listNode;
	public static Configuration config = new Configuration();
	
	public SystemIndexCentral(int indexID, int serverID, int gamma) {
		// TODO Auto-generated constructor stub
		this.indexID = indexID;
		this.serverID = serverID;
		this.gamma = gamma;
		listNode = new Hashtable<String, SystemNode>();
		listNode.put("", new SystemNode(serverID, "", 0, gamma));
	}
	
	public int getIndexID()
	{
		return this.indexID;
	}

	public void add(BF bf)
	{
		SystemNode n =  (SystemNode)listNode.get("");
		
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
				n = (SystemNode)listNode.get(o);
				o = n.add(bf);
			}
		}
	}
	
	private void split(SystemNode father, ContainerLocal c)
	{
		Iterator<BF> iterator = c.iterator();
		BF bf = c.get(0);
		Fragment f = bf.getFragment(father.getRang());
		String path = father.getPath() + "/" + f.toInt();
		SystemNode n = new SystemNode(serverID, path, father.getRang() + 1, gamma);
		father.add(bf,path);
		this.listNode.put(path, n);
		
		while (iterator.hasNext())
		{
			bf = iterator.next();
			this.add(bf);	
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object search(BF bf)
	{
		SystemNode n = (SystemNode)listNode.get("");
		ArrayList<BF> resultat = new ArrayList<BF>();
		ArrayList<Object> list = (ArrayList<Object>) n.search(bf);
		config.addNodeVisited(1);
		//systeme.Configuration.nodeVisited++;
		
		int i = 0;
		while (i < list.size())
		{
			Object o = list.get(i);
			
			if (((o.getClass()).getName()).equals("systeme.BF"))
			{
				resultat.add((BF)o);
			}else{
				SystemNode node_tmp = (SystemNode)listNode.get((String)o);
				config.addNodeVisited(1);
				//systeme.Configuration.nodeVisited++;
				
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
		SystemNode n = (SystemNode)listNode.get("");
		config.addNodeMatched("");
		//systeme.Configuration.nodeMatched.add("");
		Object o = n.searchExact(bf);
		
		while(o != null)
		{
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				n = listNode.get((String)o);
				config.addNodeVisited(1);
				//systeme.Configuration.nodeVisited++;
				config.addNodeMatched((String)o);
			//	systeme.Configuration.nodeMatched.add((String)o);
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
		SystemNode n = (SystemNode)listNode.get("");
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
				n = (SystemNode)listNode.get(path.substring(0, lastIndex));
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
						n = (SystemNode)listNode.get(path.substring(0, lastIndex));
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
		
		Enumeration<SystemNode> e = listNode.elements();
		
		while(e.hasMoreElements())
			s += (e.nextElement()).toString() + "\n";
		
		return s;
	}
	
	public void toFile(String name, boolean append)
	{		
		Enumeration<SystemNode> e = listNode.elements();
		
		File f = new File(name);
		if (f.exists() && !append)
			f.delete();
		
		while(e.hasMoreElements())
		{
			WriteFile wf = new WriteFile(name, true);
			wf.write((e.nextElement()).toString() + "\n");
			wf.close();
		}
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
