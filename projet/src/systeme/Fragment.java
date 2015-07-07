package systeme;

import java.io.Serializable;
import java.util.BitSet;

public class Fragment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BitSet bitset;
	private int size;
	
	public Fragment(int nbits)
	{
		bitset = new BitSet(nbits);
		this.size = nbits;
	}
	
	public BitSet getBitSet()
	{
		return this.bitset;
	}
	
	public void setBit(int index, boolean value)
	{
		bitset.set(index, value);
			
	}
	
	public String toString()
	{
		String s = new String();
		
		for (int i = 0; i < this.size; i++)
		{
			s += (bitset.get(i)) ? "1" : "0";
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
		
		if (this.size != other.size())
			return false;
		
		for (int i = 0; i < this.size(); i++)
			if (bitset.get(i) && !other.get(i))
				return false;
		return true;
	}
	
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		final Fragment other = (Fragment) o;
		
		if (this.size != other.size())
			return false;
		
		if (this.bitset != other.getBitSet() && (this.bitset == null || !this.bitset.equals(other.getBitSet())))
			return false;
		return true;
	}
	
	public int toInt()
	{
		int res = 0;
		for (int i = 0; i < this.size; i++)
		{
			res += this.bitset.get(i) ? (int)Math.pow(2, i) : 0;
		}
		return res;
	}
	
	public boolean get(int index)
	{
		return bitset.get(index);
	}
	
	public int size()
	{
		return this.size;
	}
	
	

}
