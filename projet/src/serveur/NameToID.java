package serveur;

import java.security.MessageDigest;

public class NameToID {
	
	private int length;
	
	public NameToID(int length) {
		// TODO Auto-generated constructor stub
		this.length = length;
	}
	
	public int translate(String s)
	{
		MessageDigest md256;
		int res = -1;
		try 
		{
			md256 = MessageDigest.getInstance("SHA-256");
			byte[] tmp = md256.digest(s.getBytes("UTF-8"));
			double n = 0;

			for (int j = 0; j < tmp.length; j++)
			{
				n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.length + n;
			}
			res =  (int) (n % this.length);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	public void setLength(int length)
	{
		this.length = length;
	}
	
	public int getLength()
	{
		return this.length;
	}

}
