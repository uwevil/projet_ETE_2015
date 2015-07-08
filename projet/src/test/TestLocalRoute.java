package test;

import exception.ErrorException;
import systeme.BF;
import systeme.ContainerLocal;
import systeme.Fragment;
import systeme.LocalRoute;

public class TestLocalRoute {

	public static void main(String[] args) throws ErrorException {
		// TODO Auto-generated constructor stub
		LocalRoute l = new LocalRoute(2);
		BF bf1 = new BF(2, 1);
		BF bf2 = new BF(2, 1);
		
		bf2.add("bbb");
		Fragment f1 = new Fragment(1);
		Fragment f2 = new Fragment(1);
		f1.setBit(0, false);
		f2.setBit(0, true);
		
		ContainerLocal containerBF1 = new ContainerLocal(2);
		ContainerLocal containerBF2 = new ContainerLocal(2);
		ContainerLocal containerBF3 = new ContainerLocal(2);
		
		System.out.println("LocalRoute l");
		System.out.println("BF bf1 = " + bf1);
		System.out.println("BF bf2 = " + bf2);
		System.out.println("Fragment f1 : valeur = " + f1.toInt());
		System.out.println("Fragment f2 : valeur = " + f2.toInt());
		System.out.println("Container containerBF1 limit = " + containerBF1.limit());
		System.out.println("Container containerBF2 limit = " + containerBF2.limit());
		System.out.println("Container containerBF3 limit = " + containerBF3.limit());
		
		containerBF1.add(bf1);
		containerBF1.add(bf2);
		System.out.println("ContainerLocal contient containerBF1: " + containerBF1 );
		System.out.println("ContainerLocal contient containerBF2: " + containerBF2 );


		l.add(f1, containerBF1);
		l.add(f2, containerBF2);
		
		ContainerLocal tmp = (ContainerLocal) l.get(f1);
		
		System.out.println("--------test contains--------------------------");
		if (tmp.contains(bf1))
			System.out.println("  Conteneur stocké dans localRoute contient bf1");
		if (l.contains(f2))
			System.out.println("  localRoute contient des données à l'entrée f2");	
		l.remove(f2);
		if(!l.contains(f2))
			System.out.println("  après la suppression l'entrée f2, localRoute ne contient plus l'entrée f2");
		
		System.out.println("--------test get(fragment f)---------");
		System.out.println("  localRoute get(fragment f1) = "+ tmp.toString());
		System.out.println("  localRoute get(fragment f2) = "+ tmp.toString());
		
		System.out.println("------ajout filtre dans conteneur plein via loalRoute------");
		BF bf3 = new BF("11", 1);
		l.add(f2, new BF("00", 1));
		l.add(f2, new BF("01", 1));
		if (!l.add(f2, bf3))
			System.out.println("  impossible d'ajouter");
		
		System.out.println("-------ajout un String------------------------");
		l.add(f2, "/00/11/11");
		System.out.println("  String stocké dans localRoute est : " + l.get(f2));	
		l.add(f2, "/00/11");
		System.out.println("  String stocké dans localRoute est : " + l.get(f2));	
		
		System.out.println("-------localRoute toString()");
		System.out.println("  " + l.toString());
		
		System.out.println("-------Test instanciation----------------------------");
		System.out.println("  containerBF1 : " + containerBF1);
		((ContainerLocal)l.get(f1)).remove(bf1);
		System.out.println("  containerBF1 : " + containerBF1);
		containerBF1.remove(bf2);
		System.out.println("  containerBF1 : " + ((ContainerLocal)l.get(f1)).toString());
		System.out.println("===> OK");

	}

}
