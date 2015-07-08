package serveur;

import java.util.ArrayList;

import systeme.Node;


public class ServerNode implements Server {
	private int nbrServeurs;
	ArrayList<Server> listServer;
	ArrayList<Node> listNode;
	
	public ServerNode(int nbrServeurs) {
		// TODO Auto-generated constructor stub
		this.nbrServeurs = nbrServeurs;
		this.listServer = new ArrayList<Server>(nbrServeurs);
		this.listNode = new ArrayList<Node>();
	}
	
	public void setNumberOfServer(int nbrServeurs)
	{
		if (nbrServeurs > this.nbrServeurs)
		{
			this.nbrServeurs = nbrServeurs;
			listServer.ensureCapacity(nbrServeurs);
		}
	}
	
	public void add(Server server)
	{
		if (!listServer.contains(server))
			listServer.add(server);
		
	}

	@Override
	public Message treatMessage(Message m) {
		// TODO Auto-generated method stub
		return null;
	}

}
