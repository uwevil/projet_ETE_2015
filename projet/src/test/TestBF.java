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
		String s = "00010001101011000010000011111001000101101101010101100101100110011100111011000011101110001011000101011110100010100010111010100010111011101101100101000110011000100111111110000110110000101001010101100010101000101101010101011110011110101111001111010001110010000110111100010111110011111100011010011110000101111110110100011011111001101000001100100101010001110101001000101101110000011101100111111110100011100011000001100000010001010111010101001011111010111001010110010110100001100001111111111000000101000101111110000010";
		BF bf = new BF(s, 512/64);
		s = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100000000000000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		BF bf2 = new BF(s, 512/64);
			
		BF bf3 = new BF(bf2.toString(), 4);
		
		
		BF bf1 = new BF(512, 512/64);
		BF test = new BF("00000000000000000100000001000000000000100000000000000000001000000000000000001010000000000000000000100000010001010000000000000000000001000000001000000000000000000100000010010000000010000000001000010000001000000000000000001000000000000000000000000010000000000000000100100000110000010100000001001000000000001000000000000000001000000000000000000000000000000000000010100000001000000000000000010000000000000010010000000000010000000000010000001000100000000010000000000100000000000000100001010000000010100000000000000000", 512/64);
		bf1.addAll("regional,north,america,united,states,oregon");
		
		bf2 = new BF(bf1.toString(), 512/64);
		
		Fragment f = bf1.getFragment(3);
		Fragment f2 = (new Fragment(0)).intToFragment(bf.getBitsPerElement(), 7);
		
		System.out.println(bf1.toString());
		System.out.println(bf2.toString());
		System.out.println(f.toString() + " " + f.toInt());
		System.out.println(f2.toString() + " " + f2.toInt());
		System.out.println("f in f2 = " + f.in(f2));
		
		System.out.println(bf1.getFragment(10).toString());
		System.out.println(test.getFragment(2).toString());
		System.out.println(bf2.equals(bf1) + " LOLL");
		
		System.out.println(bf2.in(bf1) + " "+  bf2.getClass());
		System.out.println(bf2.getClass().getName().equals(bf2.getClass().getName()));
	}

}
