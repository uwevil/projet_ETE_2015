package test;

import systeme.BF;
import systeme.Fragment;

public class TestOperateurLogique2Int {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BF bf = new BF(16, 4);
		bf.addAll("aaa,dsqd,fdg,hgfhtr,trte");
		
		Fragment f = bf.getFragment(0);
		Fragment f2 = (new Fragment(10)).intToFragment(4, 11);
		
		System.out.println(bf);
		System.out.println(f.toInt() + " " + f.toString());
		System.out.println(f2.toInt() + " " + f2.toString());
		System.out.println(f.toString() + " in "+ f2.toString() + " = " +f.in(f2));
	}

}
