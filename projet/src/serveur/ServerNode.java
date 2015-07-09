package serveur;

import java.util.ArrayList;

import systeme.Node;


public class ServerNode implements Server {
	private int ID;
	private int nbrServeurs;
	ArrayList<Server> listServer;
	ArrayList<Node> listNode;
	
	public ServerNode(int ID, int nbrServeurs) {
		// TODO Auto-generated constructor stub
		this.ID = ID;
		this.nbrServeurs = nbrServeurs;
		this.listServer = new ArrayList<Server>(nbrServeurs);
		this.listNode = new ArrayList<Node>();
	}
	
	public int getID()
	{
		return this.ID;
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
			listServer.add(server.getID(), server);
	}

	@Override
	public void treatMessage(Message m) {
		// TODO Auto-generated method stub
	}

}
