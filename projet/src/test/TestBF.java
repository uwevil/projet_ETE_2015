package test;

import exception.ErrorException;
import systeme.BF;
import systeme.Fragment;

public class TestBF {

	public TestBF() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated method stub
		BF bf = new BF("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", 512/64);
		BF bf2 = new BF("01111011111111111111101110101111000110101101110001011010110110111011101111011111111110110111111110010110101100010111000101111110111110010001111011111111111001111111111011010011110100011111001101101110011101101001011110101101001111011110111101111101001100101110101000111111111010110101010011111010001111011101111110111111111010111011111111101001011001100001110011011101111110011100010101111101110111011010000011110011011101111111111011111100011111011111110101111101101001100110011001111111111001110111001101101111", 512/64);
			
		BF bf3 = new BF(bf2.toString(), 4);
		
		
		BF bf1 = new BF(512, 512/64);
		BF test = new BF("00000000000000000100000001000000000000100000000000000000001000000000000000001010000000000000000000100000010001010000000000000000000001000000001000000000000000000100000010010000000010000000001000010000001000000000000000001000000000000000000000000010000000000000000100100000110000010100000001001000000000001000000000000000001000000000000000000000000000000000000010100000001000000000000000010000000000000010010000000000010000000000010000001000100000000010000000000100000000000000100001010000000010100000000000000000", 512/64);
		bf1.addAll("regional,north,america,united,states,oregon");
		
		
		Fragment f = bf1.getFragment(3);
		Fragment f2 = (new Fragment(0)).intToFragment(bf.getBitsPerElement(), 7);
		
		System.out.println(bf1.toString());
		System.out.println(f.toString() + " " + f.toInt());
		System.out.println(f2.toString() + " " + f2.toInt());
		System.out.println("f in f2 = " + f.in(f2));
		
		//System.out.println(bf1.getFragment(10).toString());
		//System.out.println(test.getFragment(2).toString());
		//System.out.println(bf1.in(test));
	}

}
