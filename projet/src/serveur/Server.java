package serveur;

public interface Server {
	public int getID();
	public void setNumberOfServer(int nbrServeurs);
	public void add(Server server);
	public void treatMessage(Message m);
}
