package peerSimTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

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
import systeme.LocalRoute;

public class SystemIndexProtocol implements EDProtocol{

	private static final String PAR_TRANSPORT = "transport";
	private String prefix;
	private int tid;
	private int nodeIndex;
	private Transport t;
	private int id;
	
	private Hashtable<Integer, SystemIndexP2P> listSystemIndexP2P = new Hashtable<Integer, SystemIndexP2P>();
	private int recu = 0;
	
	public SystemIndexProtocol(String prefix) {
		// TODO Auto-generated constructor stub
		this.prefix = prefix;
		tid = Configuration.getPid(prefix+ "." + PAR_TRANSPORT);
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	public void setNodeIndex(int nodeIndex)
	{
		this.nodeIndex = nodeIndex;
	}
	
	public Object clone()
	{
		SystemIndexProtocol s = new SystemIndexProtocol(prefix);
		s.tid = this.tid;
		s.prefix = this.prefix;
		s.nodeIndex = this.nodeIndex;
		s.id = this.id;
		return s;
	}
	
	@Override
	public void processEvent(Node node, int pid, Object event) {
		// TODO Auto-generated method stub
		
		t = (Transport) Network.get(nodeIndex).getProtocol(tid);
		Message message = (Message)event;
				
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
			treatRemove(message, pid);
			break;
			
		case "search": //search, Name, tableau contient BF et liste des paths
			treatSearch(message, pid);
			break;
			
		case "searchExact":
			treatSearchExact(message, pid);
			break;
			
		case "createNode": //createNode, Name, path
			treatCreateNode(message, pid);
			break;
			
		case "removeNode": //removeNode, Name, path
			break;
		 
		case "search_OK": //			
			treatSearch_OK(message, pid);
			break;
			
		case "searchExact_OK":
			treatSearchExact_OK(message, pid);
			break;
			
		case "overview": // nœud 0 balance la requete vers tous les autres nœuds
			treatOverview(message, pid);
			break;
			
		case "overview_OK": // tous les nœuds répond au nœud 0
			treatOverview_OK(message, pid);
			break;
			
		default : 
			
			break;
		}
	}


	@SuppressWarnings({ "unchecked" })
	private void treatSearch_OK(Message message, int pid)
	{
		BF bf = (BF) ((Object[])message.getData())[0];
		
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		
		ArrayList<BF> data1 = (ArrayList<BF>) ((Object[])message.getData())[1];
		
		if (treatListAnswer(message))
		{
			long time = Calendar.getInstance().getTimeInMillis() - ControlerNw.search_log.get(key).getTime();
			
			System.out.println("OKKKKKKKKK");
			System.out.println(time + "ms");
			
			WriteFile wf1 = new WriteFile(Config.peerSimLOG_resultat + "_BF_resume_"+key, true);
			wf1.write("Nombre total de filtres ajoutés : " + ControlerNw.config_log.getTotalFilterAdded() + " filtres\n");
			wf1.write("Nombre total de nœuds crées : " + ControlerNw.config_log.getNodeCreated() + " nœuds\n");
			
			wf1.write("Nombre de chemins visités : " + ControlerNw.search_log.get(key).getNodeVisited() + " nœuds\n");
			wf1.write("Nombre de chemins matched : " + ControlerNw.search_log.get(key).sizeNodeMatched() + " nœuds\n");
			
			int j = 0;
			for (int i = 0; i < Network.size(); i++)
			{
				
				if (((int[])((Object[])ControlerNw.search_log.get(key).getListAnswer(key))[0])[i] != 0)
					j++;
			}
			
			wf1.write("Nombre de pairs visités : " + j + " pairs\n");
			if (time >= 1000)
			{
				long i = time;
				long hours = TimeUnit.MILLISECONDS.toHours(i);
				i -= TimeUnit.HOURS.toMillis(hours);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(i);
				i -= TimeUnit.MINUTES.toMillis(minutes);
				long seconds = TimeUnit.MILLISECONDS.toSeconds(i);
				i -= TimeUnit.SECONDS.toMillis(seconds);
				
				wf1.write("\nTemps de recherche : " + time + "ms == " 
						+ hours + ":" + minutes + ":" + seconds + "." + i
						+ "\n\n");
			}
			else
			{
				wf1.write("\nTemps de recherche : " + time + "ms\n\n");
			}
			
			wf1.write("Liste des chemins matched: " + ControlerNw.search_log.get(key).getNodeMatched() + "\n");
			wf1.close();
			
			for (int i = 0; i < Network.size(); i++)
			{
				ControlerNw.config_log.setNodePerServer(i, 0);
				
				Message rep = new Message();
				rep.setType("overview");
				rep.setIndexName(message.getIndexName());
				rep.setSource(nodeIndex);
				rep.setDestinataire(i);
				
				t.send(Network.get(nodeIndex), Network.get(i), rep, pid);
			}
			
			ControlerNw.search_log.get(key).removeListAnswer(key);
		}
		
		
		if (data1 != null && data1.size() > 0)
		{
			ControlerNw.config_log.addNumberOfFilters(data1.size());
			String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

			/*
			WriteFile wf = new WriteFile(Config.peerSimLOG_resultat + "_"+key, true);
			wf.write(date + "       Source : " + message.getSource() + "\n");
			wf.write("        "+ data1.toString() + "\n\n");
			wf.close();
			*/
			
			WriteFile wf1 = new WriteFile(Config.peerSimLOG_resultat + "_node_" + key, true);
			wf1.write(date + "       Source : " + message.getSource() + "\n");
			wf1.write("                              " 
						+ ControlerNw.config_log.getNumberOfFilters() + " (" + data1.size() +")\n");
			wf1.close();
		}
		
	}
	
	private void treatSearchExact_OK(Message message, int pid)
	{
		
		BF data1 =(BF) ((Object[])message.getData())[1];
		
		if (data1 == null)
			return;
		
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(data1.toString());
		
		long time = ((Calendar.getInstance()).getTimeInMillis())- ControlerNw.search_log.get(key).getTime();
		System.out.println(time + "ms");
		
		String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

		WriteFile wf = new WriteFile(Config.peerSimLOG_resultat + "_BF_Exact", true);
		wf.write(date + "       Source : " + message.getSource() + "\n");
		wf.write("        "+ data1.toString() + "\n\n");
		wf.close();
	}
	
	private boolean treatListAnswer(Message message)
	{
		BF bf = (BF) ((Object[])message.getData())[0];
		
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());

		if (ControlerNw.search_log.get(key).containsKeyListAnswer(key))
		{
			Object[] o = (Object[]) ControlerNw.search_log.get(key).getListAnswer(key);
			int[] received = (int[])o[0];
			int[] total = (int[])o[1];
			
			if (message.getSource() != nodeIndex)
				received[message.getSource()] += 1;
			
			if (message.getOption1() == null)
			{
				return testOK(received, total, Network.size());
			}
			else
			{
				String s = (String) message.getOption1();
				String[] tmp = s.split(";");
				
				if (tmp.length < 1)
				{
					return testOK(received, total, Network.size());
				}
				else
				{
					for (int i = 0; i < tmp.length; i++)
					{
						if (tmp[i].length() >= 1)
						{
							int j = Integer.parseInt(tmp[i]);
							
							if (j != nodeIndex)
							{
								total[j] += 1;
							}
						}
					}
					return testOK(received, total, Network.size());
				}
			}
		}
		
		System.out.println("lollll key??? " + (new SimpleDateFormat("mm-ss-SSS")).format(new Date()));
		
		return false;	
	}
	
	private boolean testOK(int[] a, int[] b, int size)
	{
		/*
		//*******LOG*************
		String date = (new SimpleDateFormat("HH-mm-ss-SSS")).format(new Date());
		WriteFile wf = new WriteFile(Config.peerSimLOG + "_testOK", true);
		wf.write(date + "\n");
		wf.write(" received  total \n");
		
		for (int i = 0; i < Network.size(); i++)
		{
			if (a[i] != 0 || b[i] != 0)
				wf.write(i + " " + a[i] + " " + b[i] + "\n");
		}
		wf.write("\n");
		wf.close();
		//************************
		*/
		
		for (int i = 0; i < size; i++)
		{
			if (a[i] != b[i])
				return false;
		}
		return true;
	}
	
	private void treatCreateNode(Message message, int pid)
	{
		String indexName = message.getIndexName();
		String path = message.getPath();
		
		ControlerNw.config_log.getTranslate().setLength(Network.size());
		int serverID = ControlerNw.config_log.getTranslate().translate(path);
		
		ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
		int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
		
		if (serverID == nodeIndex) // c'est bien ce serveur qui gère ce path
		{	
			SystemIndexP2P systemIndex;
			if (!this.listSystemIndexP2P.containsKey(indexID)) // contient pas ce systemIndex
			{
				systemIndex = new SystemIndexP2P(indexName, serverID, Config.gamma);
				this.listSystemIndexP2P.put(indexID, systemIndex);
			}
			else // listSystemIndexP2P.containsKey(indexID)
			{
				systemIndex = this.listSystemIndexP2P.get(indexID);			
			}
			
			SystemNodeP2P systemNode = new SystemNodeP2P(serverID, path, 
					(new CalculRang()).getRang(path), Config.gamma);
			
			//****************
			int rang = systemNode.getRang();
			if (!Config.indexHeight.containsKey(rang))
			{
				Config.indexHeight.put(rang, systemNode.getPath());
			}
			
			//****************
			
			systemIndex.addSystemNodeP2P(path, systemNode);
			
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
				treatAdd(o, indexName, rep, pid);
			}
			/*
			//*******LOG*******
			WriteFile wf = new WriteFile(Config.peerSimLOG, true);
			wf.write("createNode "+ indexID + " node "+ nodeIndex + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//*****************
			 * 
			 */
		}
		else // serverID != nodeIndex
		{
			t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
			/*
			//*******LOG*******
			WriteFile wf = new WriteFile(Config.peerSimLOG, true);
			wf.write("createNode forward "+ indexID + " node "+ nodeIndex + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//*****************
			 * 
			 */
		}
		
	}
	
	private void treatAdd(Message message, int pid)
	{

		String indexName = message.getIndexName();
		String path = message.getPath();
		
		if (path.equals("")) // ajout dans la racine, c-à-d la racine est sur le serveur qui stocke systemIndex
		{
			ControlerNw.config_log.getTranslate().setLength(Network.size());	
			int serverID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			if (serverID == nodeIndex) //c'est bien le serveur racine qui gère ce systemIndex
			{
				if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex
					return;
				
				SystemIndexP2P systemeIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
				Object o = systemeIndex.add((BF)message.getData(), path);
				treatAdd(o, indexName, message, pid);
				/*
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("add "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			*/
			}
			else // serverID != nodeIndex
			{				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				/*
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("add forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			*/
			}
		}
		else // path != ""
		{ 
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int serverID = ControlerNw.config_log.getTranslate().translate(path);
			
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			if (serverID == nodeIndex) // si contient ce nœud
			{
				if (!this.listSystemIndexP2P.containsKey(indexID)) // s'il contient pas ce systemIndex, créez le
				{
					SystemIndexP2P systemIndex = new SystemIndexP2P(indexName, serverID, Config.gamma);
					systemIndex.add((BF)message.getData(), path);
					
					this.listSystemIndexP2P.put(indexID, systemIndex);
					
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("add path "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				*/
				}
				else // this.listSystemIndexP2P.containsKey(indexID)
				{
					SystemIndexP2P systemIndex = (SystemIndexP2P)this.listSystemIndexP2P.get(indexID);
					
					Object o = systemIndex.add((BF)message.getData(), path);
					treatAdd(o, indexName, message, pid);
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("add path "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
				*/
				}
			}
			else // serverID != nodeIndex
			{				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				/*
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("add path forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
			*/
			}
		}
	}
	
	private void treatAdd(Object o, String indexName, Message message, int pid)
	{
		if (o == null)
			return;
		
		if (o.getClass().getName().equals("java.lang.String")) // adresse d'un nœud
		{
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int tmp_nodeID = ControlerNw.config_log.getTranslate().translate((String)o);
			
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
			
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int tmp_nodeID = ControlerNw.config_log.getTranslate().translate(o_tmp.getPath());
			
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
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int serverID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			if (nodeIndex == message.getSource())
			{				
				ControlerNw.config_log.getTranslate().setLength(1000000);
				int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
				
				if (!ControlerNw.search_log.containsKey(key))
				{
					Config config = new Config();
					config.setTime((Calendar.getInstance()).getTimeInMillis());
					
					if (!config.containsKeyListAnswer(key))
					{
						Object[] o = new Object[2];
						int[] received = new int[Network.size()];
						int[] total = new int[Network.size()];
						
						for (int i = 0; i < Network.size(); i++)
						{
							received[i] = (0);
							total[i] = (0);
						}
						
						received[nodeIndex] = -1;
						total[nodeIndex] = -1;
						
						o[0] = received;
						o[1] = total;
						
						config.putListAnswer(key, o);
					}
					
					ControlerNw.search_log.put(key, config);
					
				}
			}
			
			if (serverID == nodeIndex) // ce serveur gère ce systemIndex
			{
				if (this.listSystemIndexP2P.containsKey(indexID)) // il contient ce systemIndex
				{
					
					//*******LOG*******
					String s_tmp = new String();
					for (int i = 0; i < Config.numberOfFragment; i++)
					{
						s_tmp += "/"+ bf.getFragment(i).toInt();
					}
					String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());
					
					ControlerNw.config_log.getTranslate().setLength(1000000);
					int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
					
					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path + "_" + key, false);
					wf1.write(date + "       Node " + nodeIndex + " receive from " + message.getSource() + "\n"
							+ "        BF " + bf.toString() + "\n"
							+ "        BF_path : " + s_tmp + "\n"
							+ "        Path : " + "" + "\n\n");
					wf1.close();
					//*****************
				
					
					SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
					
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("search racine "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					*/
					Object o = systemIndex.search(bf, "");
					
					treatSearch(o, indexName, message, pid);
				}
				else // il contient pas indexID
				{
					
					//*******LOG*******
					ControlerNw.config_log.getTranslate().setLength(1000000);
					int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
					
					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path + "_" + key, true);
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
					
					t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("search_OK "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					*/
				}
			}
			else // ce n'est pas celui qui gère ce systemIndex : serverID != nodeIndex
			{
				
				//*******LOG*******
				String s_tmp = new String();
				for (int i = 0; i < Config.numberOfFragment; i++)
				{
					s_tmp += "/"+ bf.getFragment(i).toInt();
				}
				String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

				ControlerNw.config_log.getTranslate().setLength(1000000);
				int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
				
				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path + "_" + key, true);
				wf1.write(date + "       Node " + nodeIndex + " transfer to " + serverID + "\n"
						+ "        BF " + bf.toString() + "\n"
						+ "        BF_path : " + s_tmp + "\n"
						+ "        Path : " + "" + "\n");
				wf1.close();
				//*****************
				
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				//***********listAnswers*********
				ControlerNw.config_log.getTranslate().setLength(1000000);
				int key1 = ControlerNw.config_log.getTranslate().translate(bf.toString());
				
				Object[] o = (Object[]) ControlerNw.search_log.get(key).getListAnswer(key1);
				
				int[] total = (int[])o[1];
				total[serverID] += 1;
				//*******************************
				
				/*
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("search racine forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
				*/
			}
		}
		else // search les fils : ArrayList<String>
		{
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			
			//*******LOG*******
			String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());
			
			ControlerNw.config_log.getTranslate().setLength(1000000);
			int key = ControlerNw.config_log.getTranslate().translate(bf.toString());

			WriteFile wf = new WriteFile(Config.peerSimLOG_path + "_" + key, true);
			wf.write(date +"       Node " + nodeIndex + " receive paths"
				//	+ message.toString() + "\n"
					+ "\n");
			wf.close();
			//*****************
			
			
			if (this.listSystemIndexP2P.containsKey(indexID)) // contient indexID
			{
				SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
				
				ArrayList<String> als = (ArrayList<String>) ((Object[])message.getData())[1];
				Iterator<String> iterator = als.iterator();
				
				while(iterator.hasNext())
				{
					String path_tmp = iterator.next();
					
					//*******LOG*******
					
					ControlerNw.config_log.getTranslate().setLength(1000000);
					int key1 = ControlerNw.config_log.getTranslate().translate(bf.toString());
					
					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path + "_" + key1, true);
					wf1.write("        Path : " + path_tmp + "\n");
					wf1.close();
					//*****************
					
					Object o = systemIndex.search(bf, path_tmp);
					treatSearch(o, indexName, message, pid);
				}
	
				/*
				//*******LOG*******
				WriteFile wf2 = new WriteFile(Config.peerSimLOG, true);
				wf2.write("search path "+ indexID+ " node "+ nodeIndex  + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
				*/
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

				
				//*******LOG*******
				String date1 = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());
				
				ControlerNw.config_log.getTranslate().setLength(1000000);
				int key1 = ControlerNw.config_log.getTranslate().translate(bf.toString());

				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path + "_" + key1, true);
				wf1.write(date1 + "       Node " + nodeIndex + " reply to " + message.getSource() + "\n"
						+ "\n");
				wf1.close();
				//*****************
				
				
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
				
				/*
				//*******LOG*******
				WriteFile wf2 = new WriteFile(Config.peerSimLOG, true);
				wf2.write("search_OK path "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
			*/
			}	
		}	
	}
		
	@SuppressWarnings("unchecked")
	private void treatSearch(Object o, String indexName, Message message, int pid)
	{
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(((BF) ((Object[])message.getData())[0]).toString());
		
		if (o == null)
		{
			if (message.getSource() != nodeIndex)
			{
				Object[] o_tmp = new Object[2];
				o_tmp[0] = (BF) ((Object[])message.getData())[0];
				o_tmp[1] = null;
				
				Message rep = new Message();
				rep.setType("search_OK");
				rep.setIndexName(indexName);
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				rep.setData(o_tmp);
			
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			}
			return;
		}
		
		if (message.getSource() == nodeIndex)
		{
			if (((ArrayList<BF>) ((Object[])o)[0]).size() == 0)
				return;
			
			//*******LOG*******
			
			ControlerNw.config_log.addNumberOfFilters( ((ArrayList<BF>) ((Object[])o)[0]).size() ) ;
			
			String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

			/*
			WriteFile wf = new WriteFile(Config.peerSimLOG_resultat + "_"+key, true);
			wf.write("BF source " + message.getSource() + "\n"
					+ ((BF) ((Object[])message.getData())[0]).toString() + "\n\n"
					+ ((ArrayList<BF>) ((Object[])o)[0]).toString()
					+ "\n\n");
			wf.close();
			*/
			
			WriteFile wf1 = new WriteFile(Config.peerSimLOG_resultat + "_node_" + key, true);
			wf1.write(date + "       Source : " + message.getSource() + "\n");
			wf1.write("                                  " 
					+ ControlerNw.config_log.getNumberOfFilters()
					+ " (" + ((ArrayList<BF>) ((Object[])o)[0]).size() +")\n");
			wf1.close();
			//*****************
		
		}
		else // message.getSource() != nodeIndex
		{
			ArrayList<BF> alBF = ((ArrayList<BF>) ((Object[])o)[0]);
			Hashtable<Integer, ArrayList<String>> hsials = ((Hashtable<Integer, ArrayList<String>>)((Object[])o)[1]);
			Enumeration<Integer> enumInt = hsials.keys();

			String s = new String();
			while (enumInt.hasMoreElements())
			{
				Integer i = enumInt.nextElement();
				s += i;
				for (int j = 1; j < hsials.get(i).size(); j++)
					s += ";" + i;
					
				if (enumInt.hasMoreElements())
					s += ";";
			}		
			
			Object[] o_tmp = new Object[2];
			o_tmp[0] = (BF) ((Object[])message.getData())[0];
			o_tmp[1] = alBF;
			
			Message rep = new Message();
			rep.setType("search_OK");
			rep.setIndexName(indexName);
			rep.setSource(nodeIndex);
			rep.setDestinataire(message.getSource());
			rep.setData(o_tmp);
			rep.setOption1(s);
		
			t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			
			
			//*******LOG*******
			WriteFile wf = new WriteFile(Config.peerSimLOG+"_test_"+key, true);
			wf.write("search treatSearch " + " node "+ nodeIndex + "\n"
					+ rep.toString() + "\n"
					+ "size = " + hsials.size() +"\n"
					+ "\n");
			wf.close();
			//*****************
		
		}	
		
		Hashtable<Integer, ArrayList<String>> hsials = ((Hashtable<Integer, ArrayList<String>>)((Object[])o)[1]);
		
		Enumeration<Integer> enumInt = hsials.keys();

		while (enumInt.hasMoreElements())
		{
			Message rep = new Message();
			rep.setType("search");
			rep.setIndexName(indexName);
			rep.setSource(message.getSource());
			
			Integer i = enumInt.nextElement();
			Object[] o_tmp = new Object[2];
			o_tmp[0] = (BF) ((Object[])message.getData())[0];
			ArrayList<String> o_tmp1 = new ArrayList<String>(hsials.get(i));
			o_tmp[1] = o_tmp1;

			rep.setData(o_tmp);
			rep.setDestinataire(i);

			//*******LOG*******
			String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());
			WriteFile wf1 = new WriteFile(Config.peerSimLOG_path+ "_"+ key, true);
			wf1.write(date + "       Node " + nodeIndex + " search path1 to " + i + "\n"
					+ "        Path list : " + ((Object[])rep.getData())[1].toString() + "\n"
					+ "\n");
			wf1.close();
			//*****************
			
			t.send(Network.get(nodeIndex), Network.get(i), rep, pid);
		}	
	}
	
	private void treatSearchExact(Message message, int pid)
	{		
		String indexName = message.getIndexName();
		BF bf = (BF) ((Object[])message.getData())[0];
		
		if (((Object[]) message.getData())[1] == null)
		{
			Message rep = new Message();
			rep.setType("searchExact_KO");
			rep.setIndexName(indexName);
			rep.setSource(nodeIndex);
			rep.setDestinataire(message.getSource());
			rep.setData("list path ou path == null");
			
			t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			return;
		}
		
		if ((String)((Object[]) message.getData())[1] == "") // search la racine
		{				
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int serverID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			if (nodeIndex == message.getSource())
			{				
				ControlerNw.config_log.getTranslate().setLength(1000000);
				int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
				
				if (!ControlerNw.search_log.containsKey(key))
				{
					Config config = new Config();
					config.setTime((Calendar.getInstance()).getTimeInMillis());
					
					ControlerNw.search_log.put(key, config);	
				}
			}
			
			if (serverID == nodeIndex) // ce serveur gère ce systemIndex
			{
				if (this.listSystemIndexP2P.containsKey(indexID)) // il contient ce systemIndex
				{
					/*
					//*******LOG*******
					String s_tmp = new String();
					for (int i = 0; i < Config.numberOfFragment; i++)
					{
						s_tmp += "/"+ bf.getFragment(i).toInt();
					}
					String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, false);
					wf1.write(date + "       Node " + nodeIndex + " receive from " + message.getSource() + "\n"
							+ "        BF " + bf.toString() + "\n"
							+ "        BF_path : " + s_tmp + "\n"
							+ "        Path : " + "" + "\n\n");
					wf1.close();
					//*****************
				*/
					
					SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
					
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("search racine "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					*/
					
					Object o = systemIndex.searchExact(bf, "");
					
					treatSearchExact(o, indexName, message, pid);
				}
				else // il contient pas indexID
				{
					/*
					//*******LOG*******
					
					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
					wf1.write("Node " + nodeIndex + " reply to " + message.getSource() + "\n"
							+ "\n");
					wf1.close();
					//*****************
					*/
					
					Message rep = new Message();
					rep.setIndexName(indexName);
					rep.setType("searchExact_OK");
					
					Object[] o_tmp = new Object[2];
					o_tmp[0] = bf;
					o_tmp[1] =  null;
					
					rep.setData(o_tmp);
					rep.setSource(nodeIndex);
					rep.setDestinataire(message.getSource());
					
					t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
					
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("search_OK "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					*/
				}
			}
			else // ce n'est pas celui qui gère ce systemIndex : serverID != nodeIndex
			{
				/*
				//*******LOG*******
				String s_tmp = new String();
				for (int i = 0; i < Config.numberOfFragment; i++)
				{
					s_tmp += "/"+ bf.getFragment(i).toInt();
				}
				String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
				wf1.write(date + "       Node " + nodeIndex + " transfer to " + serverID + "\n"
						+ "        BF " + bf.toString() + "\n"
						+ "        BF_path : " + s_tmp + "\n"
						+ "        Path : " + "" + "\n");
				wf1.close();
				//*****************
				*/
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				/*
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("search racine forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
				*/
			}
		}
		else // search les fils : path != ""
		{
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			/*
			//*******LOG*******
			String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

			WriteFile wf = new WriteFile(Config.peerSimLOG_path, true);
			wf.write(date +"       Node " + nodeIndex + " receive paths"
				//	+ message.toString() + "\n"
					+ "\n");
			wf.close();
			//*****************
			*/
			
			if (this.listSystemIndexP2P.containsKey(indexID)) // contient indexID
			{
				SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
				
				String path_tmp = (String) ((Object[])message.getData())[1];
									
				//*******LOG*******
				/*	
				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
				wf1.write("        Path : " + path_tmp + "\n");
				wf1.close();
				//*****************
				*/
					
				Object o = systemIndex.searchExact(bf, path_tmp);
				treatSearchExact(o, indexName, message, pid);

				/*
				//*******LOG*******
				WriteFile wf2 = new WriteFile(Config.peerSimLOG, true);
				wf2.write("search path "+ indexID+ " node "+ nodeIndex  + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
				*/
			}
			else // !this.listSystemIndexP2P.containsKey(indexID)
			{
				Message rep = new Message();
				rep.setIndexName(indexName);
				rep.setType("searchExact_OK");
				
				Object[] o_tmp = new Object[2];
				o_tmp[0] = bf;
				o_tmp[1] = null;
				
				rep.setData(o_tmp);
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				/*
				//*******LOG*******
				String date1 = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
				wf1.write(date1 + "       Node " + nodeIndex + " reply to " + message.getSource() + "\n"
						+ "\n");
				wf1.close();
				//*****************
				*/
				
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
				/*
				//*******LOG*******
				WriteFile wf2 = new WriteFile(Config.peerSimLOG, true);
				wf2.write("search_OK path "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
			*/
			}	
		}	
	}
	
	private void treatSearchExact(Object o, String indexName, Message message, int pid)
	{
		if (o == null)
		{
			if (message.getSource() != nodeIndex)
			{
				Object[] o_tmp = new Object[2];
				o_tmp[0] = (BF) ((Object[])message.getData())[0];
				o_tmp[1] = null;
				
				Message rep = new Message();
				rep.setType("searchExact_OK");
				rep.setIndexName(indexName);
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				rep.setData(o_tmp);
			
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			}
			return;
		}
			
		if (o.getClass().getName().equals("java.lang.String"))
		{
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int serverID = ControlerNw.config_log.getTranslate().translate((String)o);
			
			Object[] o_tmp = new Object[2];
			o_tmp[0] = (BF) ((Object[])message.getData())[0];
			o_tmp[1] = o;
			
			Message rep = new Message();
			rep.setType("searchExact");
			rep.setIndexName(indexName);
			rep.setSource(message.getSource());
			rep.setDestinataire(serverID);
			rep.setData(o_tmp);
		
			t.send(Network.get(nodeIndex), Network.get(serverID), rep, pid);
		}
		else // BF
		{
			if (message.getSource() == nodeIndex)
			{				
				ControlerNw.config_log.getTranslate().setLength(1000000);
				int key = ControlerNw.config_log.getTranslate().translate((((Object[])message.getData())[0]).toString());
				
				long time = (Calendar.getInstance()).getTimeInMillis() - ControlerNw.search_log.get(key).getTime();
				System.out.println(time + "ms");
				
				
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG_resultat + "_BF_Exact", true);
				wf.write("BF source " + message.getSource() + "\n"
						+ ((BF) ((Object[])message.getData())[0]).toString() + "\n\n"
						+ ((BF)o).toString()
						+ "\n\n");
				wf.close();
				//*****************
			}
			else // message.getSource() != nodeIndex
			{
				Object[] o_tmp = new Object[2];
				o_tmp[0] = (BF) ((Object[])message.getData())[0];
				o_tmp[1] = o;
				
				Message rep = new Message();
				rep.setType("searchExact_OK");
				rep.setIndexName(indexName);
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				rep.setData(o_tmp);
			
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			}
		}	
	}
	
	private void treatCreateIndex(Message message, int pid)
	{
		String indexName = message.getIndexName();
		
		ControlerNw.config_log.getTranslate().setLength(Network.size());
		int serverID = ControlerNw.config_log.getTranslate().translate(indexName);
		
		ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
		int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
		
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
				/*
				//********test********
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("createIndex "+ indexID + " node "+ nodeIndex + "\n"
						+ rep.toString()
						+ "\n");
				wf.close();
				//********************
				 */
			}
			else // not contains indexID => create
			{
				SystemIndexP2P systemIndex =  new SystemIndexP2P(indexName, serverID, Config.gamma);
				systemIndex.createRoot();
				
				listSystemIndexP2P.put(indexID,systemIndex);
				
				Message rep = new Message();
				rep.setType("createIndex_OK");
				rep.setIndexName(indexName);
				rep.setData("CREATED");
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				t.send(Network.get(nodeIndex), Network.get((int)message.getSource()), rep, pid);
/*
				//********test********
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("createIndex "+ indexID + " node "+ nodeIndex + "\n"
						+ rep.toString()
						+ "\n");
				wf.close();
				//********************
			*/
			}	
		}
		else // forward cad serverID != nodeIndex
		{ 
			
			t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
			/*
			//********test********
			WriteFile wf = new WriteFile(Config.peerSimLOG, true);
			wf.write("createIndex forward "+ indexID+ " node "+ nodeIndex  + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//********************
		*/
		}
	}
	
	private void treatRemoveIndex(Message message, int pid)
	{
		String indexName = message.getIndexName();
		
		ControlerNw.config_log.getTranslate().setLength(Network.size());
		int serverID = ControlerNw.config_log.getTranslate().translate(indexName);
		
		ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
		int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
		
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
			/*
			//********test********
			WriteFile wf = new WriteFile(Config.peerSimLOG, true);
			wf.write("removeIndex "+ indexID+ " node "+ nodeIndex  + "\n"
					+ rep.toString()
					+ "\n");
			wf.close();
			//********************
			*/
		}
		else // forward
		{
			t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
			/*
			//********test********
			WriteFile wf = new WriteFile(Config.peerSimLOG, true);
			wf.write("removeIndex forward "+ indexID+ " node "+ nodeIndex  + "\n"
					+ message.toString()
					+ "\n");
			wf.close();
			//********************
		*/
		}	
	}
	
	private void treatRemove(Message message, int pid)
	{
		String indexName = message.getIndexName();
		BF bf = (BF) message.getData();
		String path = (String) message.getPath();
		
		if (path == null)
		{
			Message rep = new Message();
			rep.setType("remove_KO");
			rep.setIndexName(indexName);
			rep.setSource(nodeIndex);
			rep.setDestinataire(message.getSource());
			rep.setData("list path ou path == null");
			
			t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
			return;
		}
		
		if (path == "") // remove à partir de la racine
		{				
			ControlerNw.config_log.getTranslate().setLength(Network.size());
			int serverID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			if (serverID == nodeIndex) // ce serveur gère ce systemIndex
			{
				if (this.listSystemIndexP2P.containsKey(indexID)) // il contient ce systemIndex
				{
					/*
					//*******LOG*******
					String s_tmp = new String();
					for (int i = 0; i < Config.numberOfFragment; i++)
					{
						s_tmp += "/"+ bf.getFragment(i).toInt();
					}
					String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, false);
					wf1.write(date + "       Node " + nodeIndex + " receive from " + message.getSource() + "\n"
							+ "        BF " + bf.toString() + "\n"
							+ "        BF_path : " + s_tmp + "\n"
							+ "        Path : " + "" + "\n\n");
					wf1.close();
					//*****************
					*/
					
					SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
					
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("search racine "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					*/
					
					Object o = systemIndex.remove(bf, "");
					
					treatRemove(o, indexName, message, pid);
				}
				else // il contient pas indexID
				{
					/*
					//*******LOG*******
					
					WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
					wf1.write("Node " + nodeIndex + " reply to " + message.getSource() + "\n"
							+ "\n");
					wf1.close();
					//*****************
					*/
					
					Message rep = new Message();
					rep.setIndexName(indexName);
					rep.setType("remove_OK");					
					rep.setData(bf);
					rep.setPath(path);
					rep.setSource(nodeIndex);
					rep.setDestinataire(message.getSource());
					
					t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
					
					/*
					//*******LOG*******
					WriteFile wf = new WriteFile(Config.peerSimLOG, true);
					wf.write("search_OK "+ indexID + " node "+ nodeIndex + "\n"
							+ message.toString()
							+ "\n");
					wf.close();
					//*****************
					*/
				}
			}
			else // ce n'est pas celui qui gère ce systemIndex : serverID != nodeIndex
			{
				/*
				//*******LOG*******
				String s_tmp = new String();
				for (int i = 0; i < Config.numberOfFragment; i++)
				{
					s_tmp += "/"+ bf.getFragment(i).toInt();
				}
				String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
				wf1.write(date + "       Node " + nodeIndex + " transfer to " + serverID + "\n"
						+ "        BF " + bf.toString() + "\n"
						+ "        BF_path : " + s_tmp + "\n"
						+ "        Path : " + "" + "\n");
				wf1.close();
				//*****************
				*/
				
				t.send(Network.get(nodeIndex), Network.get(serverID), message, pid);
				
				/*
				//*******LOG*******
				WriteFile wf = new WriteFile(Config.peerSimLOG, true);
				wf.write("search racine forward "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf.close();
				//*****************
				*/
			}
		}
		else // remove les fils : path != ""
		{
			ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
			int indexID = ControlerNw.config_log.getTranslate().translate(indexName);
			
			/*
			//*******LOG*******
			String date = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

			WriteFile wf = new WriteFile(Config.peerSimLOG_path, true);
			wf.write(date +"       Node " + nodeIndex + " receive paths"
				//	+ message.toString() + "\n"
					+ "\n");
			wf.close();
			//*****************
			*/
			
			if (this.listSystemIndexP2P.containsKey(indexID)) // contient indexID
			{
				SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
									
				//*******LOG*******
				/*	
				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
				wf1.write("        Path : " + path_tmp + "\n");
				wf1.close();
				//*****************
				*/
					
				Object o = systemIndex.remove(bf, path);
				
				treatRemove(o, indexName, message, pid);

				/*
				//*******LOG*******
				WriteFile wf2 = new WriteFile(Config.peerSimLOG, true);
				wf2.write("search path "+ indexID+ " node "+ nodeIndex  + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
				*/
			}
			else // !this.listSystemIndexP2P.containsKey(indexID)
			{
				Message rep = new Message();
				rep.setIndexName(indexName);
				rep.setType("remove_OK");
				
				rep.setData(bf);
				rep.setPath(path);
				rep.setSource(nodeIndex);
				rep.setDestinataire(message.getSource());
				
				/*
				//*******LOG*******
				String date1 = (new SimpleDateFormat("mm-ss-SSS")).format(new Date());

				WriteFile wf1 = new WriteFile(Config.peerSimLOG_path, true);
				wf1.write(date1 + "       Node " + nodeIndex + " reply to " + message.getSource() + "\n"
						+ "\n");
				wf1.close();
				//*****************
				*/
				
				t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
				
				/*
				//*******LOG*******
				WriteFile wf2 = new WriteFile(Config.peerSimLOG, true);
				wf2.write("search_OK path "+ indexID + " node "+ nodeIndex + "\n"
						+ message.toString()
						+ "\n");
				wf2.close();
				//*****************
				*/
			}
		}
	}

	private void treatRemove(Object o, String indexName, Message message, int pid)
	{
		if (o == null)
			return;
		
		String path = (String) ((Message)o).getData();
		
		ControlerNw.config_log.getTranslate().setLength(Network.size());
		int serverID = ControlerNw.config_log.getTranslate().translate(path);
		
		if ((String) ((Message)o).getOption1() == "remove") // forward
		{
			Message rep = new Message();
			rep.setIndexName(message.getIndexName());
			rep.setData(message.getData());
			rep.setPath(path);
			rep.setType("remove");
			rep.setSource(message.getSource());
			rep.setDestinataire(serverID);
			
			t.send(Network.get(nodeIndex), Network.get(serverID), rep, pid);
		}
		else // removeNode
		{
			Message rep = new Message();
			rep.setIndexName(indexName);
			rep.setData(message.getData());
			rep.setPath(path);
			rep.setType("removeNode");
			rep.setSource(message.getSource());
			rep.setDestinataire(serverID);
			
			t.send(Network.get(nodeIndex), Network.get(serverID), rep, pid);
		}	
	}

	private void treatOverview(Message message, int pid)
	{	
		ControlerNw.config_log.getTranslate().setLength(Config.indexRand);
		int indexID = ControlerNw.config_log.getTranslate().translate(message.getIndexName());
		
		if (this.listSystemIndexP2P.containsKey(indexID))
		{
			// procedure
			SystemIndexP2P systemIndex = (SystemIndexP2P) this.listSystemIndexP2P.get(indexID);
			
			System.out.println(systemIndex.size());
			
			ControlerNw.config_log.setNodePerServer(nodeIndex, systemIndex.size());
			//Config.nodePerServer[nodeIndex] =  systemIndex.size();
			
			Hashtable<String, SystemNodeP2P> htss = systemIndex.getListNode();
			Enumeration<String> htEnumeration = htss.keys();
			
			while (htEnumeration.hasMoreElements())
			{
				String s = htEnumeration.nextElement();
				
				SystemNodeP2P sn = htss.get(s);
				LocalRoute lr = sn.getLocalRoute();
				Enumeration<Integer> lrEnumeration = lr.getKeyAll();
				int size = 0;
				int j = 0;
				while (lrEnumeration.hasMoreElements())
				{
					Integer i = lrEnumeration.nextElement();
					if (lr.get(i).getClass().getName().equals("systeme.ContainerLocal"))
					{
						ContainerLocal cl = (ContainerLocal) lr.get(i);
						j += cl.getNumberOfElements();
						size++;
					}
				}
				
				Config.nodeTotal++;
				ControlerNw.config_log.addFilterPerNode((int) (j / (size == 0 ? 1 : size)));
			}
		}
		
		Message rep = new Message();
		rep.setType("overview_OK");
		rep.setSource(nodeIndex);
		rep.setDestinataire(message.getSource());
		
		t.send(Network.get(nodeIndex), Network.get(message.getSource()), rep, pid);
	}

	private void treatOverview_OK(Message message, int pid)
	{
		recu++;
		if (recu == Network.size())
		{
			WriteFile wf1 = new WriteFile(Config.peerSimLOG + "_overview", true);
			
			wf1.write("Test nodeTotal = " + Config.nodeTotal + "\n");
			if (ControlerNw.config_log.getNodeCreated() > 0)
			{
				wf1.write("Nombre de filtres stockés en moyenne sur chaque nœud : " 
						+ ControlerNw.config_log.getFilterPerNode() / ControlerNw.config_log.getNodeCreated() + " filtres\n");
			}
			else
			{
				wf1.write("Nombre de filtres stockés en moyenne sur chaque nœud : " 
						+ ControlerNw.config_log.getFilterPerNode() + " filtres (0 nœud crée)\n");
			}
			
			int j = 0;
			for (int i = 0; i < Network.size(); i++)
			{
				j += ControlerNw.config_log.getNodePerServer(i);
				//j += Config.nodePerServer[i];
			}
			
			wf1.write("Nombre moyen de nœuds par serveur : " + j + "/" +  Network.size() + " (nœuds/serveur)\n");
			wf1.close();
		}
	}
	
}











