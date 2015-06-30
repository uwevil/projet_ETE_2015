package node;

import java.io.Serializable;

public class Couple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Filter reference;
	private Filter ID;

	public Couple(Filter reference) {
		// TODO Auto-generated constructor stub
		this.reference = reference;
		ID = null;
	}
	
	public Couple(Filter reference, Filter ID) {
		// TODO Auto-generated constructor stub
		this.reference = reference;
		this.ID = ID;
	}
	
	public Filter getReference()
	{
		return this.reference;
	}
	
	public Filter getID()
	{
		return this.ID;
	}
	
	public void setReference(Filter reference)
	{
		this.reference = reference;
	}
	
	public void setID(Filter ID)
	{
		this.ID = ID;
	}
	
	public String toString()
	{
		return "(" + reference.toString() + "," + ID.toString() + ")";
	}

}
