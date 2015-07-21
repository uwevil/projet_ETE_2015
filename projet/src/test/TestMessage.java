package test;

import exception.ErrorException;
import serveur.Message;
import systeme.BF;

public class TestMessage {

	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated method stub
		Message m = new Message("add", null, null, 0, 1, 
				(new BF("111110111101", 4)).getFragment(1).toInt());
		
		System.out.println(m.toString());
	}

}
