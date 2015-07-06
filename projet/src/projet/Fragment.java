package projet;

import java.util.BitSet;

public class Fragment extends BitSet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Fragment() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Fragment(int nbits)
	{
		super(nbits);
	}
	
	public Fragment (BitSet b)
	{
		super(b.size());
		
		for (int i = 0; i < this.size(); i++)
			this.set(i, b.get(i));
	}
	
	public String toString()
	{
		String s = new String();
		
		for (int i = 0; i < this.size(); i++)
		{
			s += (this.get(i)) ? "1" : "0";
		}
		
		return s;
	}
	
	public boolean in(Object o)
	{
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		
		final Fragment other = (Fragment) o;
		
		if (this.size() != other.size())
			return false;
		
		for (int i = 0; i < this.size(); i++)
			if (this.get(i) && !other.get(i))
				return false;
		return true;
	}

}
