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
		
		int serverID;
		int indexID;
		String indexName;
		String path; 
		@SuppressWarnings("unused")
		BF bf;
	
		switch(message.getType())
		{
		case "createIndex": //createIndex, Name, sourceID, descID, option
			indexName = message.getIndexName();
			
			systeme.Configuration.translate.setLength(Network.size());
			serverID = systeme.Configuration.translate.translate(indexName);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			indexID = systeme.Configuration.translate.translate(indexName);
			
			if (serverID == nodeIndex)
			{
				if (listSystemIndexP2P.containsKey(indexID))
				{
					Message rep = new Message();
					rep.setType("createIndex_OK");
					rep.setIndexName(indexName);
					rep.setData("EXISTED");
					rep.setSource(nodeIndex);
					rep.setDestinataire(message.getSource());
					
					t.send(Network.get(nodeIndex), Network.get((int)message.getSource()), rep, pid);
					
					//********test********
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
					wf.write("createIndex "+ indexID + "\n"
							+ rep.toString()
							+ "\n");
					wf.close();
					//********************

				}
				else
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
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
					wf.write("createIndex "+ indexID + "\n"
							+ rep.toString()
							+ "\n");
					wf.close();
					//********************
				}	
			}
			else // forward
			{ 
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//********test********
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
				wf.write("createIndex forward "+ indexID + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//********************
			}
						
			break;
			
		case "removeIndex": //removeIndex, Name
			indexName = message.getIndexName();
			
			systeme.Configuration.translate.setLength(Network.size());
			serverID = systeme.Configuration.translate.translate(indexName);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			indexID = systeme.Configuration.translate.translate(indexName);
			
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
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
				wf.write("removeIndex "+ indexID + "\n"
						+ rep.toString()
						+ "\n");
				wf.close();
				//********************
				
			}
			else // forward
			{
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//********test********
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
				wf.write("removeIndex forward "+ indexID + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//********************
			}
				
			break;
			
		case "add": // add, Name, path, BF
			
			indexName = message.getIndexName();
			path = message.getPath();
			
			if (path.equals("")) // ajout dans la racine, c-à-d la racine est sur le serveur qui stocke systemIndex
			{
				systeme.Configuration.translate.setLength(Network.size());	
				serverID = systeme.Configuration.translate.translate(indexName);
				
				systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
				indexID = systeme.Configuration.translate.translate(indexName);
				
				if (serverID == nodeIndex) //c'est bien le serveur racine qui gère ce systemIndex
				{
					if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex
						break;
					
					SystemIndexP2P systemeIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
					Object o = systemeIndex.add((BF)message.getData(), path);
					treatAdd(o, indexName, message, pid);
					
					//*******LOG*******
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
					wf.write("add "+ indexID + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				}
				else // il gère pas ce systemIndex,mais il fait une geste commerciale, càd forward le mess
				{
					message.setDestinataire(serverID);
					
					t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
					
					//*******LOG*******
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
					wf.write("add forward "+ indexID + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				}
			}
			else // ajout dans nœud précis, path != ""
			{ 
				systeme.Configuration.translate.setLength(Network.size());
				serverID = systeme.Configuration.translate.translate(path);
				
				systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
				indexID = systeme.Configuration.translate.translate(indexName);
				
				if (serverID == nodeIndex) // si contient ce nœud
				{
					if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex, créez le
					{
						SystemIndexP2P systemIndex = new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
						systemIndex.add((BF)message.getData(), path);
						//*******LOG*******
						WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
						wf.write("add path "+ indexID + "\n"
								+ message.toString()
								+ "\n");
						wf.close();
						//*****************
					}
					else // il contient ce systemIndex
					{
						SystemIndexP2P systemIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
						
						Object o = systemIndex.add((BF)message.getData(), path);
						treatAdd(o, indexName, message, pid);
						
						//*******LOG*******
						WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
						wf.write("add path "+ indexID + "\n"
								+ message.toString()
								+ "\n");
						wf.close();
						//*****************
					}
				}
				else // il contient pas ce nœud, forward
				{
					message.setDestinataire(serverID);
					
					t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
					
					//*******LOG*******
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
					wf.write("add path forward "+ indexID + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				}
			}
				
			break;
			
		case "remove": //remove, Name, BF
			break;
			
		case "search": //search, Name, tableau contient BF et liste des paths
			indexName = message.getIndexName();
			path = message.getPath();
			
			if (path == "") // search la racine
			{
				systeme.Configuration.translate.setLength(Network.size());
				serverID = systeme.Configuration.translate.translate(indexName);
				
				systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
				indexID = systeme.Configuration.translate.translate(indexName);
				
				if (serverID == nodeIndex) // trouvé serveur gère ce systemIndex
				{
					if (!this.listSystemIndexP2P.containsKey(indexID))
					{
						Message rep = new Message();
						rep.setType("search_OK");
						rep.setIndexName(indexName);
						rep.setPath(path);
						rep.setSource(nodeIndex);
						rep.setDestinataire(message.getDestinataire());
						rep.setData(null);
						
						t.send(Network.get(nodeIndex), Network.get(message.getDestinataire()), rep, pid);
					}
					else
					{
						SystemIndexP2P systemIndex = this.listSystemIndexP2P.get(indexID);
						Object[] o = (Object[]) systemIndex.search((BF)message.getData(), path);
						
						ArrayList<BF> alBF = (ArrayList<BF>) o[0];
						Hashtable<Integer, ArrayList<String>> htString = (Hashtable<Integer, ArrayList<String>>)o[1];
						
						Message rep = new Message();
						rep.setType("search_OK");
						rep.setIndexName(indexName);
						rep.setPath(path);
						rep.setSource(nodeIndex);
						rep.setDestinataire(message.getDestinataire());
						rep.setData(alBF);
						
						t.send(Network.get(nodeIndex), Network.get(message.getDestinataire()), rep, pid);
						
						Enumeration<Integer> enumeration = htString.keys();
						
						rep.setType("search_path");
						rep.setIndexName(indexName);
						rep.setSource(message.getDestinataire());

						while (enumeration.hasMoreElements())
						{
							Integer i = enumeration.nextElement();
							
						}
					}
				}
				else // ce serveur ne contient pas ce systemIndex
				{
					
				}
			}
			else // search dans nœud fils
			{
				systeme.Configuration.translate.setLength(Network.size());
				serverID = systeme.Configuration.translate.translate(path);
				
				systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
				indexID = systeme.Configuration.translate.translate(indexName);
				
				if (serverID == nodeIndex) // ce serveur gère ce path
				{
					if (this.listSystemIndexP2P.containsKey(indexID)) // contient ce systemIndex
					{
						
					}
					else // il gère pas ce systemIndex, return
					{
						
					}
				}
				else // il gère pas ce path
				{
					
				}
			}
			
			
			break;
			
		case "createNode": //createNode, Name, path
			indexName = message.getIndexName();
			path = message.getPath();
			
			systeme.Configuration.translate.setLength(Network.size());
			serverID = systeme.Configuration.translate.translate(path);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			indexID = systeme.Configuration.translate.translate(indexName);
			
			if (serverID == nodeIndex) // c'est bien ce serveur qui gère ce path
			{	
				SystemIndexP2P systemIndex;
				if (!this.listSystemIndexP2P.containsKey(indexID)) // contient pas ce systemIndex
				{
					systemIndex = new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
				}
				else
				{
					systemIndex = this.listSystemIndexP2P.get(indexID);			
				}
				
				SystemNode systemNode = new SystemNode(serverID, path, 
						(new CalculRang()).getRang(path), systeme.Configuration.gamma);
				
				ContainerLocal c = (ContainerLocal) message.getData();
				Iterator<BF> iterator = c.iterator();
				
				while (iterator.hasNext())
				{
					systemNode.add((BF)iterator.next());
				}
				systemIndex.addSystemNode(path, systemNode);
				
				//*******LOG*******
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
				wf.write("createNode "+ indexID + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			}
			else
			{
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//*******LOG*******
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/21-07-peersim_log", true);
				wf.write("createNode forward "+ indexID + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			}
			
			break;
			
		case "removeNode": //removeNode, Name, path
			break;
		 
		case "OK": //
			break;
		}
	}
	
	public void setNodeIndex(int nodeIndex)
	{
		this.nodeIndex = nodeIndex;
	}
	
	private void treatAdd(Object o, String indexName, Message message, int pid)
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

}
