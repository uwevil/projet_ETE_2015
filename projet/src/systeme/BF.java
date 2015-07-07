package systeme;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.BitSet;

import exception.ErrorException;

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
	
	public BF(String chaineBits, int bitsPerElement) throws ErrorException
	{
		char[] chararray = chaineBits.toCharArray();
	
		this.bitsPerElement = bitsPerElement;
		
		this.bitset = new BitSet(chararray.length);
		this.bitSetSize = chararray.length;
		for (int i = 0; i< this.bitSetSize; i++)
		{
			if (chararray[i] != '0' && chararray[i] != '1')
			{
				throw new ErrorException("chaineBits contient des caractères spéciales");
			}
			
			if (chararray[i] == '1')
				this.bitset.set(i, true);
		}
		
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
		BitSet res = new BitSet(stop - start);
		
		for (int i = start; i < stop; i++)
			res.set(i, this.getBit(i));
				
		return res;
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
	
	public Fragment getFragment(int index)
	{
		if (index * this.bitsPerElement >= bitSetSize)
			return null;
		
		Fragment f = new Fragment(bitsPerElement);
		
		int j = 0;
		for (int i = index*bitsPerElement ; i < (index + 1)*bitsPerElement; i++)
			f.setBit(j++, this.getBit(i));
			
		return  f;
	}

}







