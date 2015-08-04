package calculRepartitionBit;

public class BBF_test {

	private int[] tab = new int[3];
	private int bitSetSize;
	
	public BBF_test(int bitSetSize)
	{
		this.bitSetSize = bitSetSize;
	}
	
	public void hash(String value) {
		long hash64 = MurmurHash.hash64(value);
		// apply the less hashing technique
		int hash1 = (int) hash64;
		int hash2 = (int) (hash64 >>> 32);
		//System.out.println("valeur hash1 du mot: "+value+ " est: "+hash1);
		//System.out.println("valeur hash2 du mot: "+value+ " est: "+hash2);
		int pos = -1;
		  for (int i = 0; i < 3;  i++) {
			  pos = (hash1+i*hash2) % bitSetSize;
			  if (pos < 0)
				  pos = ~ pos;
			  tab[i] = pos;
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
