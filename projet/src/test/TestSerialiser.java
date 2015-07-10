package test;

import systeme.Serializer;
import systeme.SystemIndex;

public class TestSerialiser {

	public static void main (String[] args)
	{
		Serializer serializable = new Serializer();
		System.out.println("Test Deserialization");
		System.out.println((((SystemIndex) serializable.readObject("/Users/dcs/vrac/test/serializer")).size()));
		System.out.println("Deserialization OK");
	}
}
