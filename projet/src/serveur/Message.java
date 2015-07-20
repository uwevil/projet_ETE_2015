package serveur;

public class Message {
	/*Type de message
	 * createIndex
	 * removeIndex
	 * add
	 * remove
	 * search
	 * 
	 * createNode
	 * removeNode
	 * 
	 * OK
	 * */
	
	private String indexName;
	private String type = "";
	private Object data = null;
	private String path;
	private Object src = null;
	private Object dest = null;
	private Object option1 = null;
	private Object option2 = null;
	
	public Message()
	{
	}
	
	public Message(String type, String indexName, String path, int rang, Object data1, Object data2, Object data3)
	{
		this.indexName = indexName;
		this.type = type;
		this.data = data1;
		this.src = data2;
		this.dest = data3;
		this.path = path;
	}
	
	public String getIndexName()
	{
		return this.indexName;
	}
	
	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public Object getData()
	{
		return this.data;
	}
	
	public void setData(Object data)
	{
		this.data = data;
	}
	
	public Object getSource()
	{
		return this.src;
	}
	
	public void setSource(Object src)
	{
		this.src = src;
	}
	
	public Object getDestinataire()
	{
		return this.dest;
	}
	
	public void setDestinataire(Object dest)
	{
		this.dest = dest;
	}
	
	public Object getOption1()
	{
		return this.option1;
	}
	
	public void setOption1(Object option1)
	{
		this.option1 = option1;
	}
	
	public Object getOption2()
	{
		return this.option2;
	}
	
	public void setOption2(Object option2)
	{
		this.option2 = option2;
	}
	
	
	public String toString()
	{
		return "Message \n  "
				+ "Type : " + this.getType() + "\n  "
				+ "Data : " + this.getData() + "\n  "
				+ "Source : " + this.getSource() + "\n  "
				+ "Destinataire : " + this.getDestinataire() + "\n  "
				+ "Option1 : " + this.option1 + "\n  "
				+ "Option2 : " + this.option2 + "\n";
	}
	
}
