package projet;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.BitSet;

public class BF implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BitSet bitset;
	private int bitsPerElement;
	private int bitSetSize;
	
	public BF(int bitSetSize, int bitsPerElement)
	{
		this.bitset = new BitSet(bitSetSize);
		this.bitSetSize = bitSetSize;
		this.bitsPerElement = bitsPerElement;
	}
	
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		final BF other = (BF) o;
		
		if (this.bitSetSize != other.bitSetSize)
			return false;
		if (this.bitsPerElement != other.bitsPerElement)
			return false;
		if (this.bitset != other.bitset && (this.bitset == null || !this.bitset.equals(other.bitset)))
			return false;
		return true;
	}
	
	public void addAll(String description)
	{		
		String[] s = description.split(",");
		
		MessageDigest md256, md512, md;
		try
		{
			md = MessageDigest.getInstance("SHA-1");
			md256 = MessageDigest.getInstance("SHA-256");
			md512 = MessageDigest.getInstance("SHA-512");

			for (int i = 0; i < s.length; i++)
			{
				byte[] tmp = md.digest(s[i].getBytes("UTF-8"));
				double n = 0;

				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				bitset.set((int) (n % this.bitSetSize), true);
				
				tmp = md256.digest(s[i].getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				bitset.set((int) (n % this.bitSetSize), true);
				
				tmp = md512.digest(s[i].getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				bitset.set((int) (n % this.bitSetSize), true);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void add(String a)
	{
		MessageDigest md256, md512, md;
		try
		{
			md = MessageDigest.getInstance("SHA-1");
			md256 = MessageDigest.getInstance("SHA-256");
			md512 = MessageDigest.getInstance("SHA-512");

				byte[] tmp = md.digest(a.getBytes("UTF-8"));
				double n = 0;

				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				bitset.set((int) (n % this.bitSetSize), true);
				
				tmp = md256.digest(a.getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				bitset.set((int) (n % this.bitSetSize), true);
				
				tmp = md512.digest(a.getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				bitset.set((int) (n % this.bitSetSize), true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setBit(int index, boolean v)
	{
		bitset.set(index, v);
	}
	
	public boolean getBit(int index)
	{
		return bitset.get(index);
	}
	
	public int size()
	{
		return bitSetSize;
	}
	
	public BitSet getBitSet(int start, int stop)
	{
		return bitset.get(start, stop);
	}
	
	public int getBitsPerElement()
	{
		return this.bitsPerElement;
	}
	
	public String toString()
	{
		String s = new String();
		
		for (int i = 0; i < bitSetSize; i++)
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
		final BF other = (BF) o;
		
		if (this.bitSetSize != other.bitSetSize)
			return false;
		if (this.bitsPerElement != other.bitsPerElement)
			return false;
		
		for (int i = 0; i < bitSetSize; i++)
			if (this.bitset.get(i) && !other.getBit(i))
				return false;
		return true;
	}
	

}







