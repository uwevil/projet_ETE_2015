package serveur;

import java.security.MessageDigest;

public class NameToServerID {
	
	private int nbrServeur;
	
	public NameToServerID(int nbrServeur) {
		// TODO Auto-generated constructor stub
		this.nbrServeur = nbrServeur;
	}
	
	public int translate(String s)
	{
		MessageDigest md;
		int res = -1;
		try 
		{
			md = MessageDigest.getInstance("SHA-1");
			byte[] tmp = md.digest(s.getBytes("UTF-8"));
			double n = 0;

			for (int j = 0; j < tmp.length; j++)
			{
				n = ((double)(tmp[j] & 0x000000FF)*Math.pow(2, j*8)) % this.nbrServeur + n;
			}
			res =  (int) (n % this.nbrServeur);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return res;
	}

}
