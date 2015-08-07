package peerSimTest_v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class LocalContainerP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int limit;
	private ArrayList<BFP2P> container;
	
	/* Un conteneur local doit stocker la limite(gamma) et une liste de filtre
	 * */
	
	public LocalContainerP2P(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		container = new ArrayList<BFP2P>(limit);
	}
	
	/* 
	 * Mettre la limite du conteneur
	 * */
	
	public int limit()
	{
		return this.limit;
	}
	
	/* 
	 * Rendre le nombre d'éléments stockés dans le conteneur
	 * */
	
	public int getNumberOfElements()
	{
		return this.container.size();
	}
	
	/* 
	 * Ajouter un filtre 'e' dans le conteneur
	 * Si réussi, renvoyer true
	 * Si échec, renvoyer false
	 * */
	
	public boolean add(BFP2P e)
	{
		if (this.container.size() == this.limit)
		{
			container.add(e);
			return false;
		}
			
		if (!this.container.contains(e))
		{
			container.add(e);
			return true;
		}
		return true;
	}
	
	/* 
	 * Renvoyer true si ce conteneur est vide, false sinon
	 * */
	
	public boolean isEmpty()
	{
		return this.container.isEmpty();
	}
	
	/* 
	 * Rendre le filtre à la position 'index' dans la liste des filtres
	 * */
	
	public BFP2P get(int index)
	{
		if (index >= this.limit)
			return null;
		
		try
		{
			container.get(index);
		} catch (Exception e){
			return null;
		}
		
		return container.get(index);
	}
	
	/* 
	 * Supprimer un filtre dans le conteneur
	 * */
	
	public void remove(BFP2P e)
	{
		if (!container.contains(e))
			return;
		container.remove(e);
	}
	
	/* 
	 * Renvoyer true si le filtre 'e' existe dans la liste, false sinon
	 * */
	
	public boolean contains(BFP2P e)
	{
		return container.contains(e);
	}
	
	/* 
	 * Renvoyer true si le filtre à la position 'index' existe dans la liste, false sinon
	 * */
	
	public boolean contains(int index)
	{
		try
		{
			container.get(index);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public String toString()
	{
		String s = new String();
		Iterator<BFP2P> iterator = container.iterator();
		while(iterator.hasNext())
		{
			BFP2P bf = iterator.next();
			s += "   " +bf + "\n";
			/*if (iterator.hasNext())
				s += ",";
			*/
		}

		return s;
	}
	
	public Iterator<BFP2P> iterator()
	{
		return container.iterator();
	}

}
