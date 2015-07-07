package test;

import exception.ErrorException;
import systeme.BF;
import systeme.Fragment;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated method stub
		BF bf = new BF("1111000000101010", 4);
		BF bf2 = new BF(16, 4);
	
		bf2.addAll("aaaa,dsq,gdg");
		
		BF bf3 = new BF(bf2.toString(), 4);
		
		Fragment f = bf.getFragment(0);
		Fragment f2 = bf2.getFragment(3);
		
		
		System.out.println(bf.toString());
		System.out.println(bf2.toString());
		System.out.println(bf3.toString());
		System.out.println(f.toString() + " " + f.toInt());
		System.out.println(f2.toString() + " " + f2.toInt());
	}

}
