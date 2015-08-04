package test;

import peerSimTest_v1.BFP2P;
import exception.ErrorException;

public class TestBF {

	public TestBF() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated method stub
		
		BFP2P bf1 = new BFP2P(512, 512/64);
		BFP2P bf2 = new BFP2P(512, 512/64);
		bf1.add("lattera");
		bf2.add("skrytaya");
		
		System.out.println(bf1.toString());
		System.out.println(bf2.toString());
		
		System.out.println(bf2.equals(bf1) + " LOLL");
		
	}

}
