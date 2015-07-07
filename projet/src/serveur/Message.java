package serveur;

public class Message {
	private String type;
	private Object data;
	private Server src;
	private Server dest;
	
	public Message(String type, Object data, Server src, Server dest)
	{
		this.type = type;
		this.data = data;
		this.src = src;
		this.dest = dest;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public Object getData()
	{
		return this.data;
	}
	
	public Server getSrc()
	{
		return this.src;
	}
	
	public Server getDest()
	{
		return this.dest;
	}
	
}
