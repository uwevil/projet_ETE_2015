package test;

import systeme.Serializer;
import systeme.SystemIndexCentral;

public class TestSerialiser {

	public static void main (String[] args)
	{
		Serializer serializable = new Serializer();
		System.out.println("Test Deserialization");
		SystemIndexCentral s1 = (SystemIndexCentral) serializable.readObject("/Users/dcs/vrac/test/serializer");
		SystemIndexCentral s2 = (SystemIndexCentral) serializable.readObject("/Users/dcs/vrac/test/serializer1");
		System.out.println("Deserialization OK");
		
		System.out.println("s1 et s2 sont égaux??? => " + s1.equals(s2));
	}
}
