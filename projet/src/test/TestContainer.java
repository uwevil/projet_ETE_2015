package test;

import systeme.BF;
import systeme.ContainerLocal;

public class TestContainer {

	public static void main(String[] args) {
		// TODO Auto-generated constructor stub
		ContainerLocal containerBF = new ContainerLocal(2);
		ContainerLocal containerBF2 = new ContainerLocal(2);
		
		System.out.println("containerBF limit = " + containerBF.limit());
		System.out.println("containerInt limit = " + containerBF2.limit());
		
		BF bf1 = new BF(2, 1);
		BF bf2 = new BF(2, 1);
		containerBF.add(bf1);
		containerBF.add(bf2);
		
		System.out.println("---------test d'insertion 2 éléments égaux-----------");
		System.out.println("bf1 = " + bf1);
		System.out.println("bf2 = " + bf2);
		System.out.println("containerBF (limit = " + containerBF.limit() 
				+ ") a " + containerBF.getNumberOfElements() + " éléments car 2 filtres sont égaux");
		bf2.add("wtfff");
		System.out.println("bf2 modif = " + bf2);
		containerBF.add(bf2);
		System.out.println("containerBF (limit = " + containerBF.limit() 
				+ ") a " + containerBF.getNumberOfElements() + " éléments car 2 filtres sont différents");
		
		System.out.println("containerBF contient " 
				+ containerBF.get(0) + "; "
				+ containerBF.get(1));
		
		System.out.println("----------test la limite-------------------------------");
		BF bf3 = new BF(2, 1);
		bf3.add("bbb");
		System.out.println("bf3 = " + bf3);
		if (containerBF.add(bf3)) 
		{
			System.out.println("faut pas arriver");
		}else{
			System.out.println("algo OK");
		}
		
		System.out.println("containerBF contient " 
				+ containerBF.getNumberOfElements() + " éléments "
				+ containerBF.get(0) + "; "
				+ containerBF.get(1));
		
		System.out.println("----------test de suppression d'un BF-----------------");
		containerBF.remove(bf2);
		System.out.println("après la suppression bf2");
		System.out.println("containerBF contient " 
				+ containerBF.getNumberOfElements() + " éléments "
				+ containerBF.get(0));
		containerBF.add(bf3);
		System.out.println("après le rajout bf3");
		System.out.println("containerBF contient " 
				+ containerBF.getNumberOfElements() + " éléments "
				+ containerBF.get(0) + "; "
				+ containerBF.get(1));
		
		System.out.println("---------test get null élément");
		if (containerBF2.get(2) == null)
			System.out.println("it's OK, no exception null element");
		
		System.out.println("---------test containerInt---------------------------");
		System.out.println("containerInt a " 
				+ containerBF2.getNumberOfElements() + " éléments");
		System.out.println("containerInt contient " + containerBF2.get(0));
		System.out.println("--------test contains(int index)--------------------");
		if (containerBF2.contains(1))
		{
			System.out.println("contains KO");
		}else{
			System.out.println("contains OK");
		}
	}

}
