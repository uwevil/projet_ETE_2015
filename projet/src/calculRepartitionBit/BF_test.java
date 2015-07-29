package calculRepartitionBit;

import java.io.Serializable;
import java.security.MessageDigest;

public class BF_test implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int[] tab = new int[3];
	private int bitSetSize;
	
	public BF_test(int bitSetSize)
	{
		this.bitSetSize = bitSetSize;
		for (int i = 0; i < 3; i++)
			tab[i] = 0;
	}
	
	public void add(String a)
	{
		if (a.equals(""))
			return;
		
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
				
				tab[0] = (int)(n % this.bitSetSize);
				
				tmp = md256.digest(a.getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}

				tab[1] = (int)(n % this.bitSetSize);
				
				tmp = md512.digest(a.getBytes("UTF-8"));
				n = 0;
				for (int j = 0; j < tmp.length; j++)
				{
					n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.bitSetSize + n;
				}
				
				tab[2] = (int)(n % this.bitSetSize);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int get0()
	{
		return this.tab[0];
	}
	
	public int get1()
	{
		return this.tab[1];
	}
	
	public int get2()
	{
		return this.tab[2];
	}
}







