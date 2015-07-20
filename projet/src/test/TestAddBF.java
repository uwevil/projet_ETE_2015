package test;

import exception.ErrorException;
import systeme.BF;
import systeme.SystemIndexCentral;

public class TestAddBF {
	private static int gamma = 1;
	private static int bitSetSize = 512;
	private static int bitsPerElement = 512/64;
	
	public static void main(String[] args)throws ErrorException {
		// TODO Auto-generated constructor stub
		SystemIndexCentral systemIndex = new SystemIndexCentral(0, null, gamma);
		
		
		String chaineBits = "00000001001010000000100011010000000010101010000000001001001100000100010000010000000010010100100000001000000000000000000000000000000000100001000000000000000000000100000000000001000010000000000000000000001010000000100010001000000000000000011000000010000000000010010000000000000000010000010011000000000000001000010000000000001110000000010000000000000000000000000010100010000000000000010000000000000001000010000000000000010000010000010000001000110001000100000010000000000000000000100001010000010010100000000100000100";
		String description = "regional,north,america,united,states,oregon,localities,newport,travel,tourism,lodging,vacation,rentals,little,creek,cove,condominiums,hotel,accomodations,resort,coast.,eyes,bethany,village,www.littlecreekcove.com";
		
		BF bf = new BF(bitSetSize, bitsPerElement);
		BF bf2 = new BF(chaineBits, bitsPerElement);
		BF bf3 = new BF(bitSetSize, bitsPerElement);
		
		bf.addAll(description);
		bf3.addAll(description);
		bf3.setBit(19, true);
		
		System.out.println(bf);
		System.out.println(bf2);
		System.out.println(bf3);
		
		systemIndex.add(bf);
		systemIndex.add(bf2);
		systemIndex.add(bf3);
		
		
		System.out.println(systemIndex.overView());
		System.out.println(systemIndex.searchExact(bf));
		System.out.println(systemIndex.searchExact(bf2));
		System.out.println(systemIndex.searchExact(bf3));

	}
}
