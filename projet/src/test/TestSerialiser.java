package test;

import systeme.Serializer;
import systeme.SystemIndex;

public class TestSerialiser {

	public static void main (String[] args)
	{
		Serializer serializable = new Serializer();
		System.out.println("Test Deserialization");
		SystemIndex s1 = (SystemIndex) serializable.readObject("/Users/dcs/vrac/test/serializer");
		SystemIndex s2 = (SystemIndex) serializable.readObject("/Users/dcs/vrac/test/serializer1");
		System.out.println("Deserialization OK");
		
		System.out.println("s1 et s2 sont égaux??? => " + s1.equals(s2));
	}
}
