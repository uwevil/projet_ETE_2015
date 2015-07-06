package test;

import projet.BF;
import projet.Fragment;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BF bf = new BF(512, 512/64);
		BF req = new BF(512, 512/64);
		
		bf.addAll("aaaa,bbb,dsqd,sdq,gfdg,ytu(§,dterterg");
		req.add("aaaa");
		req.add("bb");
		
		Fragment bf1 = new Fragment(bf.getFragment(63));
		Fragment req1 = new Fragment(req.getFragment(63));
		
		System.out.println(bf.toString());
		System.out.println(req.toString());
		
		System.out.println(bf1.toString());
		System.out.println(req1.toString());
		
		if (req.in(bf))
			System.out.println("innnn loll");
		
		if (req1.in(bf1))
			System.out.println("frag in lolll");
	}

}
