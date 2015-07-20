package peerSimTest;

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
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
					wf.write("EXISTE "+ indexID 
							+ " name " + message.getData() + "\n"
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
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
					wf.write("CREATED "+ indexID 
							+ " name " + message.getData() + "\n"
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
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
				wf.write("Forward "+ indexID 
						+ " name " + message.getData() + "\n"
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
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
				wf.write("Remove "+ indexID 
						+ " name " + message.getData() + "\n"
						+ rep.toString()
						+ "\n");
				wf.close();
				//********************
				
			}
			else // forward
			{
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//********test********
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
				wf.write("Forward remove "+ indexID 
						+ " name " + message.getData() + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//********************
			}
				
			break;
			
		case "add": // add, Name, path, BF
			
			indexName = message.getIndexName();
			path = message.getPath();
			
			systeme.Configuration.translate.setLength(Network.size());	
			serverID = systeme.Configuration.translate.translate(indexName);
			
			systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
			indexID = systeme.Configuration.translate.translate(indexName);
			
			if (path.equals("")) // ajout dans la racine, c-à-d la racine est sur le serveur qui stocke systemIndex
			{
				if (serverID == nodeIndex) //c'est bien le serveur racine qui gère ce systemIndex
				{
					if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex
						break;
					
					SystemIndexP2P systemeIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
					Object o = systemeIndex.add((BF)message.getData(), path);
					treatAdd(o, indexName, message, pid);
				}
				else // il gère pas ce systemIndex,mais il fait une geste commerciale, càd forward le mess
				{
					message.setDestinataire(serverID);
					
					t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				}
			}
			else // ajout dans nœud précis, path != ""
			{ 
				systeme.Configuration.translate.setLength(Network.size());
				serverID = systeme.Configuration.translate.translate(path);
				
				if (serverID == nodeIndex) // si contient ce nœud
				{
					if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex, créez le
					{
						SystemIndexP2P systemIndex = new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
						systemIndex.add((BF)message.getData(), path);
					}
					else // il contient ce systemIndex
					{
						SystemIndexP2P systemIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
						
						Object o = systemIndex.add((BF)message.getData(), path);
						treatAdd(o, indexName, message, pid);	
					}
				}
				else // il contient pas ce nœud, forward
				{
					message.setDestinataire(serverID);
					
					t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				}
			}
				
			break;
			
		case "remove": //remove, Name, BF
			break;
			
		case "search": //search, Name, BF
			break;
			
		case "createNode": //createNode, Name, path
			indexName = message.getIndexName();
			path = message.getPath();
			
			systeme.Configuration.translate.setLength(Network.size());
			serverID = systeme.Configuration.translate.translate(path);
			
			if (serverID == nodeIndex) // c'est bien ce serveur qui gère ce path
			{
				systeme.Configuration.translate.setLength(systeme.Configuration.indexRand);
				indexID = systeme.Configuration.translate.translate(indexName);
				
				if (!this.listSystemIndexP2P.containsKey(indexID)) // contient pas ce systemIndex
				{
					SystemIndexP2P systemIndex = new SystemIndexP2P(indexName, serverID, systeme.Configuration.gamma);
					SystemNode systemNode = new SystemNode(serverID, path, 
							(new CalculRang()).getRang(path), systeme.Configuration.gamma);
					
					ContainerLocal c = (ContainerLocal) message.getData();
					Iterator<BF> iterator = c.iterator();
					
					while (iterator.hasNext())
					{
						systemNode.add((BF)iterator.next());
					}
				}
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
