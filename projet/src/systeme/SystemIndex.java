package systeme;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class SystemIndex {
	private int indexID;
	private int gamma;
	private Hashtable<String, Node> listNode;
	
	public SystemIndex(int indexID, int gamma) {
		// TODO Auto-generated constructor stub
		this.indexID = indexID;
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
		while (o != null)
		{
			if ((o.getClass().getName()).equals("systeme.ContainerLocal"))
			{
				this.split(n, (ContainerLocal)o);
				break;
			}
			else if ((o.getClass().getName()).equals("java.lang.String"))
			{
				n = (Node)listNode.get(o);
				o = n.add(bf);
			}
		}
	}
	
	private void split(Node father, ContainerLocal c)
	{
		Iterator<BF> iterator = c.iterator();
		
		while (iterator.hasNext())
		{
			BF bf = iterator.next();
			Fragment f = bf.getFragment(father.getRang());
			Node n = new Node(null, father.getPath()+"/"+f.toInt(), father.getRang() + 1, gamma);
			n.add(bf);
			father.getLocalRoute().add(f, father.getPath()+"/"+f.toInt());
			this.listNode.put(father.getPath()+"/"+f.toInt(), n);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Object search(BF bf)
	{
		Node n = (Node)listNode.get("");
		ArrayList<BF> resultat = new ArrayList<BF>();
		ArrayList<Object> list = (ArrayList<Object>) n.search(bf);
		
		int i = 0;
		while (i < list.size())
		{
			Object o = list.get(i);
			
			if (o.getClass().getName().equals("systeme.BF"))
			{
				resultat.add((BF)o);
			}else{
				Node node_tmp = (Node)listNode.get((String)o);
				list.addAll((ArrayList<Object>) node_tmp.search(bf));
			}
			i++;
		}
		
		return resultat;
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