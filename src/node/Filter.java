package node;

import java.io.Serializable;
import java.security.MessageDigest;

import simulateur.Simulateur;

public class Filter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char[] filter;
	
	public Filter(String index)
	{
		this.filter = index.toCharArray();
	}
	
	public Filter(char[] filter)
	{
		this.filter = filter;
	}
	
	public Filter(String description, int size)
	{
		filter = new char[size];
		
		String[] s = description.split(",");
		for (int i = 0; i < size; i++)
		{
			filter[i] = '0';
		}
		
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
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % size + n;
				}
				filter[(int) (n % size)] = '1';
				
				tmp = md256.digest(s[i].getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % size + n;
				}
				filter[(int) (n % size)] = '1';
				
				tmp = md512.digest(s[i].getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % size + n;
				}
				filter[(int) (n % size)] = '1';
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
	}
	
	public boolean in(String a)
	{
		char[] b = a.toCharArray();
		for (int i = 0; i < filter.length; i++)
		{
			if (((int)filter[i] & (int)b[i]) != (int)filter[i])
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean in(char[] b)
	{
		for (int i = 0; i < filter.length; i++)
		{
			if (((int)filter[i] & (int)b[i]) != (int)filter[i])
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean in(Filter b)
	{
		char[] tmp = b.toString().toCharArray();
		for (int i = 0; i < filter.length; i++)
		{
			if (((int)filter[i] & (int)tmp[i]) != (int)filter[i])
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean isFilter()
	{
		return this.filter.length == Simulateur.SIZE;
	}
	
	public Filter createIndex(int index)
	{
		String tmp = filter.toString();
		int taille = Simulateur.SIZE / Simulateur.FRAGMENTS;
	
		String tmp2 = tmp.substring(taille*index, taille*index + taille);
		
		return new Filter(tmp2);
	}
	
	public String toString()
	{
		return new String(filter);
	}

}
