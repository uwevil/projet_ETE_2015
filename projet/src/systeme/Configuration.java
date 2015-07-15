package systeme;

import java.util.ArrayList;

public class Configuration {
	public static int nodeVisited = 0;
	public static ArrayList<String> nodeMatched = new ArrayList<String>();
	public static int doublon = 0;
	public static int numberOfBF = 0;
	public static int sizeOfBF = 512;
	public static int numberOfFragment = 64;
	public static int gamma = 1000;
	
	public Configuration()
	{
		nodeVisited = 0;
		nodeMatched = new ArrayList<String>();
		doublon = 0;
		numberOfBF = 0;
	}
}
