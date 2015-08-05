package test;

import peerSimTest_v2.BFP2P;
import peerSimTest_v2.BFToPath;
import peerSimTest_v2.PathToBF;
import exception.ErrorException;

public class TestBF {

	public TestBF() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated method stub
		
		int sizeOfFragment = 512/64;
		
		BFP2P bf1 = new BFP2P("01010101"
							+ "01010000"
							+ "11111111"
							+ "11111111"
							+ "11111111"
							+ "11111110", 512/64);
		BFP2P bf2 = new BFP2P(bf1.getFragment(0).toString(), 512/64);
		BFP2P bf3 = new BFP2P(bf1.getFragment(1).toString()+"11111111111111111111111111111111", 512/64);
		
		BFP2P bf4 = new BFP2P(bf2.toString()+bf3.toString(), 512/64);

		System.out.println(bf1.toString());
		System.out.println(bf2.toString());
		System.out.println(bf3.toString());
		System.out.println(bf4.toString());

		System.out.println(bf1.equals(bf4));
		System.out.println(bf1.getFragment(0).toInt());
		
		String s = "/170/10/255";
		String s2 = "/255/255/127";
		BFP2P test1 = (new PathToBF(s, sizeOfFragment)).convert();
		BFP2P test2 = (new PathToBF(bf1.getFragment(1).toInt()+"", sizeOfFragment)).convert();

		System.out.println(test1);
		System.out.println(test2);

		System.out.println(test1.equals(bf2));
		
		System.out.println((new BFToPath(bf1, sizeOfFragment)).convert());
		System.out.println((new PathToBF(s+s2, sizeOfFragment)).convert().equals(bf1));
	}

}
