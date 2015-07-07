package test;

import projet.BF;
import projet.Fragment;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BF bf = new BF(512, 512/2);
		BF bf2 = new BF(512, 512/2);
		
		bf.addAll("aaaa,bbb,dsqd,sdq,gfdg,ytu(§,dterterg");
		bf2.addAll("aaaa,bbb");

		System.out.println(bf.toString());
		System.out.println(bf2.toString());
		
		Fragment f = bf2.getFragment(1);
		System.out.println(bf2.toString());
		System.out.println(bf.getFragment(1));
		
		if (f.in(bf.getFragment(1)))
			System.out.println("ok");
		
		if (bf2.in(bf))
			System.out.println("okkk");
	}

}
