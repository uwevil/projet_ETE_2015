package peerSimTest;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
import serveur.Message;
import test.WriteFile;
import systeme.BF;
import systeme.CalculRang;
import systeme.ContainerLocal;
import systeme.SystemNode;

public class SystemIndexProtocol implements EDProtocol{

	private static final String PAR_TRANSPORT = "transport";
	private String prefix;
	private int tid;
	private int nodeIndex;
	private Transport t;
	
	private Hashtable<Integer, SystemIndexP2P> listSystemIndexP2P = new Hashtable<Integer, SystemIndexP2P>();
	private Hashtable<Integer, Object[]> listAnswers = new Hashtable<Integer, Object[]>();
	
	public SystemIndexProtocol(String prefix) {
		// TODO Auto-generated constructor stub
		this.prefix = prefix;
		tid = Configuration.getPid(prefix+ "." + PAR_TRANSPORT);
	}
	
	public Object clone()
	{
		SystemIndexProtocol s = new SystemIndexProtocol(prefix);
		s.tid = this.tid;
		s.prefix = this.prefix;
		s.nodeIndex = this.nodeIndex;
		return s;
	}
	
	@Override
	public void processEvent(Node node, int pid, Object event) {
		// TODO Auto-generated method stub
		
		t = (Transport) Network.get(nodeIndex).getProtocol(tid);
		Message message = (Message)event;
		
		/*
		if (message.getDestinataire() == 35)
		{
		//*******LOG*******
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG+"–test", true);
				wf.write("recu " + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
		}
			*/
				
		switch(message.getType())
		{
		case "createIndex": //createIndex, Name, sourceID, descID, option
			treatCreateIndex(message, pid);		
			break;
			
		case "removeIndex": //removeIndex, Name
			treatRemoveIndex(message, pid);
			break;
			
		case "add": // add, Name, path, BF
			treatAdd(message, pid);
			break;
			
		case "remove": //remove, Name, BF
			break;
			
		case "search": //search, Name, tableau contient BF et liste des paths
			treatSearch(message, pid);
			break;
			
		case "createNode": //createNode, Name, path
			treatCreateNode(message, pid);
			break;
			
		case "removeNode": //removeNode, Name, path
			break;
		 
		case "search_OK": //			
			treatSearch_OK(message, pid);
			break;
			
		}
	}
	
	private void treatSearch_OK(Message message, int pid) 
	{
		BF bf = (BF) ((Object[])message.getData())[0];
		
		systeme.Configuration.translate.setLength(19876);
		int key = systeme.Configuration.translate.translate(bf.toString());
		
		Object[] data = (Object[])message.getData();
		ArrayList<BF> data1 = (ArrayList<BF>) data[1];
		
		if (data1 != null)
		{
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG_resultat + "_BF", true);
			wf.write(data1.toString());
			wf.close();
		}
		/*
		if (this.listAnswers.containsKey(key))
		{
			Object[] o1 = this.listAnswers.get(key);
			ArrayList<BF> o2 = (ArrayList<BF>)o1[0];
			Integer i1 = (Integer)o1[1];
			Integer i2 = (Integer)o1[2];
			
			Object[] data = (Object[])message.getData();
			ArrayList<BF> data1 = (ArrayList<BF>) data[1];
			
			if (data1 != null)
			{
				o2.addAll((Collection<? extends BF>) ((Object[])message.getData())[1]);
			}
			i1 += (int) message.getOption1();
			i2++;
			
			if (i1 == i2)
			{
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG_resultat, true);
				wf.write("Requete : " + bf.toString() + "\n");
				wf.write(o2.toString());
				wf.close();
				
				this.listAnswers.remove(key);
			}
		}
		else // not contains key
		{
			Object[] tab = new Object[3];
			tab[0] = new ArrayList<BF>();
			tab[1] = new Integer(1);
			tab[2] = new Integer(0);
			
			if (((Object[])message.getData())[1] != null)
				((ArrayList<BF>) tab[0]).addAll((Collection<? extends BF>) ((Object[])message.getData())[1]);
		
			tab[1] =(int) tab[1] + (int) message.getOption1();
			tab[2] = (int) tab[2] + 1;
			
			if ((int)tab[1] == (int)tab[2])
			{
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG_resultat, false);
				wf.write("Requete : " + bf.toString() + "\n");
				wf.write(((ArrayList<BF>)tab[0]).toString());
				wf.close();
				break;
			}
			this.listAnswers.put(key, tab);
		}	
		*/		
		
	}

	private void treatCreateNode(Message message, int pid) 
	{
		String indexName = message.getIndexName();
		String path = message.getPath();
		
		systeme.Configuration.translate.setLength(Network.size());
		int serverID = systeme.Configuration.translate.translate(path);
		
		systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
		int indexID = systeme.Configuration.translate.translate(indexName);
		
		if (serverID == nodeIndex) // c'est bien ce serveur qui gère ce path
		{	
			SystemIndexP2P systemIndex;
			if (!this.listSystemIndexP2P.containsKey(indexID)) // contient pas ce systemIndex
			{
				systemIndex = new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
				this.listSystemIndexP2P.put(indexID, systemIndex);
			}
			else // listSystemIndexP2P.containsKey(indexID)
			{
				systemIndex = this.listSystemIndexP2P.get(indexID);			
			}
			
			SystemNode systemNode = new SystemNode(serverID, path, 
					(new CalculRang()).getRang(path), systeme.Configuration.gamma);
			
			systemIndex.addSystemNode(path, systemNode);
			
			ContainerLocal c = (ContainerLocal) message.getData();
			Iterator<BF> iterator = c.iterator();
			
			Message rep = new Message();
			rep.setType("add");
			rep.setIndexName(indexName);
			rep.setPath(path);
			rep.setDestinataire(message.getDestinataire());
			rep.setSource(message.getSource());
			
			while (iterator.hasNext())
			{
				BF bf = (BF)iterator.next();
				rep.setData(bf);
				Object o = systemIndex.add(bf, path);
				treatAdd1(o, indexName, rep, pid);
			}
			
			//*******LOG*******
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
			wf.write("createNode "+ indexID + " node "+ nodeIndex + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//*****************
		}
		else // serverID != nodeIndex
		{
			t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
			
			//*******LOG*******
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
			wf.write("createNode forward "+ indexID + " node "+ nodeIndex + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//*****************
		}
		
	}

	public void setNodeIndex(int nodeIndex)
	{
		this.nodeIndex = nodeIndex;
	}
	
	private void treatAdd(Message message, int pid)
	{

		String indexName = message.getIndexName();
		String path = message.getPath();
		
		if (path.equals("")) // ajout dans la racine, c-à-d la racine est sur le serveur qui stocke systemIndex
		{
			systeme.Configuration.translate.setLength(Network.size());	
			int serverID = systeme.Configuration.translate.translate(indexName);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			int indexID = systeme.Configuration.translate.translate(indexName);
			
			if (serverID == nodeIndex) //c'est bien le serveur racine qui gère ce systemIndex
			{
				if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex
					return;
				
				SystemIndexP2P systemeIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
				Object o = systemeIndex.add((BF)message.getData(), path);
				treatAdd1(o, indexName, message, pid);
				
				//*******LOG*******
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf.write("add "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			}
			else // serverID != nodeIndex
			{				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//*******LOG*******
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf.write("add forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			}
		}
		else // path != ""
		{ 
			systeme.Configuration.translate.setLength(Network.size());
			int serverID = systeme.Configuration.translate.translate(path);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			int indexID = systeme.Configuration.translate.translate(indexName);
			
			if (serverID == nodeIndex) // si contient ce nœud
			{
				if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex, créez le
				{
					SystemIndexP2P systemIndex = new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
					systemIndex.add((BF)message.getData(), path);
					
					this.listSystemIndexP2P.put(indexID, systemIndex);
					
					//*******LOG*******
					WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
					wf.write("add path "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				}
				else // this.listSystemIndexP2P.containsKey(indexID)
				{
					SystemIndexP2P systemIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
					
					Object o = systemIndex.add((BF)message.getData(), path);
					treatAdd1(o, indexName, message, pid);
					
					//*******LOG*******
					WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
					wf.write("add path "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				}
			}
			else // serverID != nodeIndex
			{				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//*******LOG*******
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf.write("add path forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			}
		}
	}
	
	private void treatAdd1(Object o, String indexName, Message message, int pid)
	{
		if (o == null)
			return;
		
		if (o.getClass().getName().equals("java.lang.String")) // adresse d'un nœud
		{
			systeme.Configuration.translate.setLength(Network.size());
			int tmp_nodeID = systeme.Configuration.translate.translate((String)o);
			
			Message rep = new Message();
			rep.setType("add");
			rep.setIndexName(indexName);
			rep.setPath((String) o);
			rep.setData(message.getData());
			rep.setSource(nodeIndex);
			rep.setDestinataire(tmp_nodeID);
			
			t.send(Network.get(nodeIndex), Network.get(tmp_nodeID), rep, pid);
		}
		else // Message split()
		{
			Message o_tmp = (Message)o;
			
			systeme.Configuration.translate.setLength(Network.size());
			int tmp_nodeID = systeme.Configuration.translate.translate(o_tmp.getPath());
			
			Message rep = new Message();
			rep.setType("createNode");
			rep.setIndexName(indexName);
			rep.setPath(o_tmp.getPath());
			rep.setData(o_tmp.getData());
			rep.setSource(nodeIndex);
			rep.setDestinataire(tmp_nodeID);
			
			t.send(Network.get(nodeIndex), Network.get(tmp_nodeID), rep, pid);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void treatSearch(Message message, int pid)
	{
		systeme.Configuration.time = System.currentTimeMillis();
		
		String indexName = message.getIndexName();
		BF bf = (BF) ((Object[])message.getData())[0];
		
		if (((Object[]) message.getData())[1] == null)
		{
			Message rep = new Message();
			rep.setType("search_KO");
			rep.setIndexName(indexName);
			rep.setSource(nodeIndex);
			rep.setDestinataire(message.getSource());
			rep.setData("list path ou path == null");
			
			t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			return;
		}
			
		if (((Object[]) message.getData())[1].getClass().getName().equals("java.lang.String")) // search la racine
		{	
			systeme.Configuration.translate.setLength(Network.size());
			int serverID = systeme.Configuration.translate.translate(indexName);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			int indexID = systeme.Configuration.translate.translate(indexName);
			
			if (serverID == nodeIndex) // ce serveur gère ce systemIndex
			{
				if (this.listSystemIndexP2P.containsKey(indexID)) // il contient ce systemIndex
				{
					//*******LOG*******
					String s_tmp = new String();
					for (int i = 0; i < systeme.Configuration.numberOfFragment; i++)
					{
						s_tmp += "/"+ bf.getFragment(i).toInt();
					}
					
					WriteFile wf1 = new WriteFile(systeme.Configuration.peerSimLOG_path, false);
					wf1.write("Node " + nodeIndex + " receive from " + message.getSource() + "\n"
							+ "BF " + bf.toString() + "\n"
							+ "BF_path : " + s_tmp + "\n"
							+ "  Path : " + "" + "\n");
					wf1.close();
					//*****************
					
					SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
					
					//*******LOG*******
					WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
					wf.write("search racine "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					
					Object o = systemIndex.search(bf, "");
					
					treatSearch1(o, indexName, message, pid);
				}
				else // il contient pas indexID
				{
					
					//*******LOG*******
					
					WriteFile wf1 = new WriteFile(systeme.Configuration.peerSimLOG_path, true);
					wf1.write("Node " + nodeIndex + " reply to " + message.getSource() + "\n"
							+ "\n");
					wf1.close();
					//*****************
					
					Message rep = new Message();
					rep.setIndexName(indexName);
					rep.setType("search_OK");
					
					Object[] o_tmp = new Object[2];
					o_tmp[0] = bf;
					o_tmp[1] =  null;
					
					rep.setData(o_tmp);
					rep.setSource(nodeIndex);
					rep.setDestinataire(message.getSource());
					rep.setOption1(0);
					
					t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
					
					//*******LOG*******
					WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
					wf.write("search_OK "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					
				}
			}
			else // ce n'est pas celui qui gère ce systemIndex : serverID != nodeIndex
			{
				//*******LOG*******
				String s_tmp = new String();
				for (int i = 0; i < systeme.Configuration.numberOfFragment; i++)
				{
					s_tmp += "/"+ bf.getFragment(i).toInt();
				}
				
				WriteFile wf1 = new WriteFile(systeme.Configuration.peerSimLOG_path, true);
				wf1.write("Node " + nodeIndex + " transfer to " + serverID + "\n"
						+ "BF " + bf.toString() + "\n"
						+ "BF_path : " + s_tmp + "\n"
						+ "  Path : " + "" + "\n");
				wf1.close();
				//*****************
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//*******LOG*******
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf.write("search racine forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
				
			}
		}
		else // search les fils : ArrayList<String>
		{
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			int indexID = systeme.Configuration.translate.translate(indexName);
			
			//*******LOG*******
			
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG_path, true);
			wf.write("Node " + nodeIndex + " receive paths" + "\n"
					+ "\n");
			wf.close();
			//*****************
			
			if (this.listSystemIndexP2P.containsKey(indexID)) // contient indexID
			{
				SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
				
				ArrayList<String> als = ((ArrayList<String>) ((Object[])message.getData())[1]);
				Iterator<String> iterator = als.iterator();
				
				while(iterator.hasNext())
				{
					String path_tmp = iterator.next();
					
					//*******LOG*******
					WriteFile wf1 = new WriteFile(systeme.Configuration.peerSimLOG_path, true);
					wf1.write("  Path : " + path_tmp + "\n");
					wf1.close();
					//*****************
					
					Object o = systemIndex.search(bf, path_tmp);
					treatSearch1(o, indexName, message, pid);
				}
				
				//*******LOG*******
				WriteFile wf2 = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf2.write("search path "+ indexID+ " node "+ nodeIndex  + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
				
			}
			else // !this.listSystemIndexP2P.containsKey(indexID)
			{
				Message rep = new Message();
				rep.setIndexName(indexName);
				rep.setType("search_OK");
				
				Object[] o_tmp = new Object[2];
				o_tmp[0] = bf;
				o_tmp[1] = null;
				
				rep.setData(o_tmp);
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				rep.setOption1(0);
				
				//*******LOG*******
				
				WriteFile wf1 = new WriteFile(systeme.Configuration.peerSimLOG_path, true);
				wf1.write("Node " + nodeIndex + " reply to " + message.getSource() + "\n"
						+ "\n");
				wf1.close();
				//*****************
				
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
				
				//*******LOG*******
				WriteFile wf2 = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf2.write("search_OK path "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
			}	
		}	
	}
	
	
	@SuppressWarnings("unchecked")
	private void treatSearch1(Object o, String indexName, Message message, int pid)
	{
		if (o == null)
			return;
		
		if (message.getSource() == nodeIndex)
		{
			//*******LOG*******
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG_resultat + "_BF", true);
			wf.write("BF " + "\n"
					+ ((BF) ((Object[])message.getData())[0]).toString() + "\n\n"
					+ ((ArrayList<BF>) ((Object[])o)[0]).toString()
					+ "\n");
			wf.close();
			//*****************
		}
		else // message.getSource() != nodeIndex
		{
			ArrayList<BF> alBF = ((ArrayList<BF>) ((Object[])o)[0]);
			Hashtable<Integer, ArrayList<String>> hsials = ((Hashtable<Integer, ArrayList<String>>)((Object[])o)[1]);
						
			Object[] o_tmp = new Object[2];
			o_tmp[0] = (BF) ((Object[])message.getData())[0];
			o_tmp[1] = alBF;
			
			Message rep = new Message();
			rep.setType("search_OK");
			rep.setIndexName(indexName);
			rep.setSource(nodeIndex);
			rep.setDestinataire(message.getSource());
			rep.setData(o_tmp);
			rep.setOption1(hsials.size());
		
			t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);

			//t.send(Network.get(nodeIndex), Network.get(rep.getDestinataire()), rep, pid);
			
			//*******LOG*******
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
			wf.write("search treatSearch " + " node "+ nodeIndex + "\n"
					+ rep.toString()
					+ "\n");
			wf.close();
			//*****************
		}
				
		
		
		/*
		//*******LOG*******
		WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG_resultat + "_BF", true);
		wf.write("BF " + "\n"
				+ o_tmp[0].toString() + "\n"
				+((ArrayList<BF>)o_tmp[1]).toString()
				+ "\n");
		wf.close();
		//*****************
		*/
		
		Hashtable<Integer, ArrayList<String>> hsials = ((Hashtable<Integer, ArrayList<String>>)((Object[])o)[1]);
		
		Enumeration<Integer> enumInt = hsials.keys();
		
		Object[] o_tmp = new Object[2];
		o_tmp[0] = (BF) ((Object[])message.getData())[0];

		Message rep = new Message();
		rep.setType("search");
		rep.setIndexName(indexName);
		rep.setSource(message.getSource());
		
		while (enumInt.hasMoreElements())
		{
			Integer i = enumInt.nextElement();
			o_tmp[1] = hsials.get(i);
			
			rep.setData(o_tmp);
			rep.setDestinataire(i);

			//*******LOG*******
			
			WriteFile wf1 = new WriteFile(systeme.Configuration.peerSimLOG_path, true);
			wf1.write("Node " + nodeIndex + " search path to " + i + "\n"
					+ "Path list : " + hsials.get(i).toString() + "\n"
					+ "\n");
			wf1.close();
			//*****************
			
			t.send(Network.get(nodeIndex), Network.get(i), rep, pid);
		}	
	}
	
	private void treatCreateIndex(Message message, int pid)
	{
		String indexName = message.getIndexName();
		
		systeme.Configuration.translate.setLength(Network.size());
		int serverID = systeme.Configuration.translate.translate(indexName);
		
		systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
		int indexID = systeme.Configuration.translate.translate(indexName);
		
		if (serverID == nodeIndex)
		{
			if (listSystemIndexP2P.containsKey(indexID)) // contains indexID
			{
				Message rep = new Message();
				rep.setType("createIndex_OK");
				rep.setIndexName(indexName);
				rep.setData("EXISTED");
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				t.send(Network.get(nodeIndex), Network.get((int)message.getSource()), rep, pid);
				
				//********test********
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf.write("createIndex "+ indexID + " node "+ nodeIndex + "\n"
						+ rep.toString()
						+ "\n");
				wf.close();
				//********************

			}
			else // not contains indexID => create
			{
				SystemIndexP2P systemIndex =  new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
				systemIndex.createRoot();
				
				listSystemIndexP2P.put(indexID,systemIndex);
				
				Message rep = new Message();
				rep.setType("createIndex_OK");
				rep.setIndexName(indexName);
				rep.setData("CREATED");
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				t.send(Network.get(nodeIndex), Network.get((int)message.getSource()), rep, pid);

				//********test********
				WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
				wf.write("createIndex "+ indexID + " node "+ nodeIndex + "\n"
						+ rep.toString()
						+ "\n");
				wf.close();
				//********************
			}	
		}
		else // forward cad serverID != nodeIndex
		{ 
			
			t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
			
			//********test********
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
			wf.write("createIndex forward "+ indexID+ " node "+ nodeIndex  + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//********************
		}
	}
	
	private void treatRemoveIndex(Message message, int pid)
	{
		String indexName = message.getIndexName();
		
		systeme.Configuration.translate.setLength(Network.size());
		int serverID = systeme.Configuration.translate.translate(indexName);
		
		systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
		int indexID = systeme.Configuration.translate.translate(indexName);
		
		if (serverID == nodeIndex)
		{	
			Message rep = new Message();
			if (listSystemIndexP2P.containsKey(indexID))
			{
				listSystemIndexP2P.remove(indexID);
				rep.setType("removeIndex_OK");
				rep.setIndexName(indexName);
				rep.setData("REMOVED");
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				t.send(Network.get(nodeIndex), Network.get((int)message.getSource()), rep, pid);
			}
			else
			{
				rep.setType("removeIndex_OK");
				rep.setIndexName(indexName);
				rep.setData("NOT_EXISTED");
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				t.send(Network.get(nodeIndex), Network.get((int)message.getSource()), rep, pid);
			}
			
			//********test********
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
			wf.write("removeIndex "+ indexID+ " node "+ nodeIndex  + "\n"
					+ rep.toString()
					+ "\n");
			wf.close();
			//********************
			
		}
		else // forward
		{
			t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
			
			//********test********
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG, true);
			wf.write("removeIndex forward "+ indexID+ " node "+ nodeIndex  + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//********************
		}	
	}

}











