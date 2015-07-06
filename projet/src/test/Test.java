package test;

import projet.BF;

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
		
		System.out.println(bf.toString());
		System.out.println(req.toString());
		
		if (req.in(bf))
			System.out.println("innnn loll");
	}

}
