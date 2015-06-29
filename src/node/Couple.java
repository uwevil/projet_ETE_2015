package node;

import java.io.Serializable;

public class Couple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Filter reference;
	private int IP = -1;

	public Couple(Filter reference) {
		// TODO Auto-generated constructor stub
		this.reference = reference;
	}
	
	public Couple(Filter reference, int IP) {
		// TODO Auto-generated constructor stub
		this.reference = reference;
		this.IP = IP;
	}
	
	public Filter getReference()
	{
		return this.reference;
	}
	
	public int getIP()
	{
		return this.IP;
	}
	
	public void setReference(Filter reference)
	{
		this.reference = reference;
	}
	
	public void setIP(int IP)
	{
		this.IP = IP;
	}
	
	public String toString()
	{
		return "(" + reference.toString() + "," + IP + ")";
	}

}
