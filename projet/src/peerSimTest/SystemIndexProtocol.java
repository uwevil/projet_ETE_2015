package peerSimTest;

import java.util.Hashtable;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
import serveur.Message;
import serveur.NameToID;
import test.WriteFile;

public class SystemIndexProtocol implements EDProtocol{

	private static final String PAR_TRANSPORT = "transport";
	private String prefix;
	private int tid;
	private int nodeIndex;
	
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
		Transport t = (Transport) Network.get(nodeIndex).getProtocol(tid);
		Message message = (Message)event;
		
		NameToID n;
		int serverID;
		int indexID;
	
		switch(message.getType())
		{
		case "createIndex": //createIndex, Name, sourceID, descID, option
			n = new NameToID(Network.size());	
			serverID = n.translate((String) message.getData1());
			n.setLength(999999);
			indexID = n.translate((String) message.getData1());
			
			if (serverID == nodeIndex)
			{
				if (listSystemIndexP2P.containsKey(indexID))
				{
					Message rep = new Message();
					rep.setType("createIndex_OK");
					rep.setData1(false);
					rep.setData2(nodeIndex);
					rep.setData3(message.getData2());
					
					t.send(Network.get(nodeIndex), Network.get((int)message.getData2()), rep, pid);
					
					
					//********test********
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
					wf.write("EXISTE "+ indexID 
							+ " name " + message.getData1() 
							+ " on server " + serverID 
							+ " created by Node " + nodeIndex 
							+ " >====< " 
							+ message.getData2()
							+ ">>>>>"
							+ nodeIndex
							+ "\n");
					wf.close();
					//********************

				}else{
					listSystemIndexP2P.put(indexID, new SystemIndexP2P(indexID, serverID, systeme.Configuration.gamma));
					Message rep = new Message();
					rep.setType("createIndex_OK");
					rep.setData1(true);
					rep.setData2(nodeIndex);
					rep.setData3(message.getData2());
					
					t.send(Network.get(nodeIndex), Network.get((int)message.getData2()), rep, pid);
					

					//********test********
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
					wf.write("CREATED "+ indexID 
							+ " name " + message.getData1() 
							+ " on server " + serverID 
							+ " by Node " + nodeIndex 
							+ " >====< " 
							+ message.getData2()
							+ ">>>>>"
							+ nodeIndex
							+ "\n");
					wf.close();
					//********************
				}	
			}else{
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				
				//********test********
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
				wf.write("Forward "+ indexID 
						+ " name " + message.getData1() 
						+ " on server " + serverID 
						+ " created by Node " + nodeIndex 
						+ " >====< " 
						+ message.getData2()
						+ ">>>>>"
						+ nodeIndex
						+ "\n");
				wf.close();
				//********************
			}
						
			break;
			
		case "removeIndex": //removeIndex, Name
			
			n = new NameToID(Network.size());	
			serverID = n.translate((String) message.getData1());
			n.setLength(999999);
			indexID = n.translate((String) message.getData1());
			
			if (serverID == nodeIndex)
			{
				if (listSystemIndexP2P.containsKey(indexID))
				{
					listSystemIndexP2P.remove(indexID);
					Message rep = new Message();
					rep.setType("removeIndex_OK");
					rep.setData1(true);
					rep.setData2(nodeIndex);
					rep.setData3(message.getData2());
					
					t.send(Network.get(nodeIndex), Network.get((int)message.getData2()), rep, pid);
				}else{
					Message rep = new Message();
					rep.setType("removeIndex_OK");
					rep.setData1(false);
					rep.setData2(nodeIndex);
					rep.setData3(message.getData2());
					
					t.send(Network.get(nodeIndex), Network.get((int)message.getData2()), rep, pid);
				}
				//********test********
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
				wf.write("Remove "+ indexID 
						+ " name " + message.getData1() 
						+ " on server " + serverID 
						+ " created by Node " + nodeIndex 
						+ " >====< " 
						+ message.getData2()
						+ ">>>>>"
						+ nodeIndex
						+ "\n");
				wf.close();
				//********************
				
			}else{
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//********test********
				WriteFile wf = new WriteFile("/Users/dcs/vrac/test/peersim_log", true);
				wf.write("Forward remove "+ indexID 
						+ " name " + message.getData1() 
						+ " on server " + serverID 
						+ " created by Node " + nodeIndex 
						+ " >====< " 
						+ message.getData2()
						+ ">>>>>"
						+ nodeIndex
						+ "\n");
				wf.close();
				//********************
			}
				
			break;
			
		case "add": // add, Name, BF
			break;
			
		case "remove": //remove, Name, BF
			break;
			
		case "search": //search, Name, BF
			break;
			
		case "createNode": //createNode, Name, path
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

}
