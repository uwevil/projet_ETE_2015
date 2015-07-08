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
	 * forward
	 * 
	 * OK
	 * */
	private String type = "";
	private Object data1 = null;
	private Object data2 = null;
	private Object data3 = null;
	
	public Message()
	{
	}
	
	public Message(String type, Object data1, Object data2, Object data3)
	{
		this.type = type;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public Object getData1()
	{
		return this.data1;
	}
	
	public void setData1(Object data1)
	{
		this.data1 = data1;
	}
	
	public Object getData2()
	{
		return this.data2;
	}
	
	public void setData2(Object data2)
	{
		this.data2 = data2;
	}
	
	public Object getData3()
	{
		return this.data3;
	}
	
	public void setData3(Object data3)
	{
		this.data3 = data3;
	}
	
	public String toString()
	{
		return "message \n  "
				+ "Type : " + this.getType() + "\n  "
				+ "Data1 : " + this.getData1() + "\n  "
				+ "Data2 : " + this.getData2() + "\n  "
				+ "Data3 : " + this.getData3() + "\n";
	}
	
}
