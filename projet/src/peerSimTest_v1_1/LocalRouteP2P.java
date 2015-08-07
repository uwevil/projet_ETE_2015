package peerSimTest_v1_1;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class LocalRouteP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* LocalRoute contient soit un Container soit un String 
	 * */
	private Hashtable<Integer, Object> localRoute;
	
	/* Initialiser avec la limite (gamma) pour chaque conteneur et une hashtable 'localRoute'
	 * 
	 * localRoute contient soit une chaîne de caractères soit un conteneur local 
	 * 	identifiés par la valeur du fragment en entier
	 * */
	
	public LocalRouteP2P() {
		// TODO Auto-generated constructor stub
		localRoute = new Hashtable<Integer, Object>();
	}
	
	/*
	 * Ajouter le filtre 'bf' à l'entrée correspondante au fragment f
	 * 
	 * Renvoyer true si réussit, false sinon
	 * */
	
	public boolean add(FragmentP2P f, BFP2P bf)
	{		
		if (!this.contains(f))
		{
			LocalContainerP2P c = new LocalContainerP2P();
			c.add(bf);
			localRoute.put(f.toInt(), c);
			return true;
		}else{
			if ((((this.get(f)).getClass()).getName()).equals("java.lang.String"))
			{
				return false;
			}else{
				return ((LocalContainerP2P) this.get(f)).add(bf);
			}
		}
	}
	
	/* 
	 * Ajouter une chaîne de caractère 'path' à l'entrée correspondante au frament 'f'
	 * */
	
	public void add(FragmentP2P f, String path)
	{
		if (!this.contains(f))
		{
			localRoute.put(f.toInt(), path);
		}else{
			localRoute.remove(f.toInt());
			localRoute.put(f.toInt(), path);
		}
	}
	
	/* 
	 * Rendre une valeur correspondante à l'entrée 'f' dans la table de routage
	 * */
	
	public Object get(FragmentP2P f)
	{
		if (this.contains(f))
		{
			return localRoute.get(f.toInt());
		}else{
			return null;
		}
	}
	
	/* 
	 * Rendre une valeur correspondantes à la position 'index' dans la table de routage
	 * */
	
	public Object get(int index)
	{
		if (this.contains(index))
		{
			return localRoute.get(index);
		}else{
			return null;
		}
	}
	
	/* 
	 * Renvoyer true si la table de routage contient une valeur à la position 'f'
	 * */
	
	public boolean contains(FragmentP2P f)
	{
		return localRoute.containsKey(f.toInt());
	}
	
	/* 
	 * Renvoyer true si la table de routage contient une valeur à la position 'index'
	 * */
	
	public boolean contains(int index)
	{
		return localRoute.containsKey(index);
	}
	
	/* 
	 * Supprimer l'entrée 'f' dans la table de routage
	 * */
	
	public void remove(FragmentP2P f)
	{
		this.localRoute.remove(f.toInt());
	}
	
	/* 
	 * Supprimer l'entrée 'index' dans la table de routage
	 * */
	
	public void remove(int index)
	{
		this.localRoute.remove(index);
	}
	
	/* 
	 * Rendre une liste des entrées non vides dans la table de routage
	 * */
	
	public Enumeration<Integer> getKeyAll()
	{
		return localRoute.keys();
	}
	
	public String toString()
	{
		String s = new String();
		
		Enumeration<Integer> e = localRoute.keys();
		
		while(e.hasMoreElements())
		{
			Integer o = e.nextElement();
			s += "  ContainerLocalP2P n° " + o.toString() + " : " + localRoute.get(o).toString() + "\n";
		}		
		return s;
	}
	
	public boolean isEmpty()
	{
		return localRoute.isEmpty();
	}

}
