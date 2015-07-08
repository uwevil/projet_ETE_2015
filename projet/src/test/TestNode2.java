package test;

import exception.ErrorException;
import serveur.Message;
import serveur.ServerRoot;
import systeme.BF;
import systeme.Node;

public class TestNode2 {

	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated method stub
		Message m = new Message("add", new BF("0011", 1), null, null);
		Node node1 = new Node(new ServerRoot(), "/", 0, 2);
		
		System.out.println((node1.treatMessage(m)).toString());
	}

}
