package test;

import serveur.NameToID;
import systeme.SystemNode;

public class TestNode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NameToID translate = new NameToID(3);
		NameToID tmp = new NameToID(3);
		
		System.out.println("/ : " + translate.translate("/"));
		System.out.println("/ : " + tmp.translate("/"));
		System.out.println("/00 : " + translate.translate("/00"));
		System.out.println("/00 : " + tmp.translate("/00"));
		System.out.println("/01 : " + translate.translate("/01"));
		System.out.println("/10 : " + translate.translate("/10"));
		System.out.println("/11 : " + translate.translate("/11"));
		System.out.println("/00/00 : " + translate.translate("/00/00"));
		System.out.println("/00/01 : " + translate.translate("/00/01"));
		System.out.println("/00/10 : " + translate.translate("/00/10"));
		System.out.println("/00/11 : " + translate.translate("/00/11"));
		System.out.println("/00/00/00 : " + translate.translate("/00/00/00"));
		System.out.println("/00/00/01 : " + translate.translate("/00/00/01"));
		System.out.println("/00/00/10 : " + translate.translate("/00/00/10"));
		System.out.println("/00/00/11 : " + translate.translate("/00/00/11"));
		System.out.println("/00/01/00 : " + translate.translate("/00/01/00"));
		System.out.println("/00/10/00 : " + translate.translate("/00/10/00"));

		SystemNode node1 = new SystemNode(2333, "/", 0, 2);
		System.out.println("node1 limit : " + node1.getLimit());
		System.out.println("node1 localRoute : " + node1.getLocalRoute());








	}

}
