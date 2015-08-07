package peerSimTest_v1_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class SystemNodeP2P implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int server;
	private String path;
	private int rang;
	private LocalRouteP2P localRoute;
	
	/*
	 * Initiliser un nœud avec le server hébergé, l'identifiant sous forme une chaîne de caractères, le rang et la limite
	 * 	une talbe de routage
	 * */
	
	
	public SystemNodeP2P(int server, String path, int rang) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.path = path;
		this.rang = rang;
		localRoute = new LocalRouteP2P();
	}
	
	/*
	 * Rendre l'identifiant de ce nœud
	 * */
	
	public String getPath()
	{
		return this.path;
	}
	
	/*
	 * Rendre le server hébergé
	 * */
	
	public int getServer()
	{
		return this.server;
	}
	
	/*
	 * Rendre le rang de ce nœud
	 * */
	
	public int getRang()
	{
		return this.rang;
	}
	
	/*
	 * Rendre la table de routage de ce nœud
	 * */
	
	public LocalRouteP2P getLocalRoute()
	{
		return this.localRoute;
	}
	
	/*
	 * Ajouter le filtre dans le nœud
	 * 
	 * Retourner null si réussit, sinon soit une chaîne de caractères soit un conteneur local
	 * */
	
	public Object add(BFP2P bf)
	{	
		FragmentP2P f = bf.getFragment(rang);
		
		if(localRoute.add(f, bf))
		{
			return null;
		}

		return localRoute.get(f);
	}
	
	/*
	 * Ajouter une chaîne de caractère à l'entrée correspondante au filtre 'bf'
	 * */
	
	public void add(BFP2P bf, String path)
	{
		localRoute.add(bf.getFragment(rang), path);
	}
	
	/*
	 * Rechercher tous les filtres qui contiennent le filtre de la requete 'bf'
	 * 
	 * Retourner une liste mélangée de filtres et de chaîne de caractères(chemin)
	 * */
	
	public Object search(BFP2P bf)
	{
		FragmentP2P f = bf.getFragment(rang);
		ArrayList<Object> rep = new ArrayList<Object>();
		Enumeration<Integer> list = localRoute.getKeyAll();
		
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		
		while (list.hasMoreElements())
		{
			Integer i = list.nextElement();
			FragmentP2P f_tmp = (new FragmentP2P(0)).intToFragment(bf.getBitsPerElement(), i);
			
			if (f.in(f_tmp))
			{	
				Object bf_tmp = localRoute.get(i);
				
				if (((bf_tmp.getClass()).getName()).equals("java.lang.String"))
				{
					rep.add(bf_tmp);
				}
				else
				{
					LocalContainerP2P c = (LocalContainerP2P) bf_tmp;
					Iterator<BFP2P> iterator = c.iterator();
					
					while (iterator.hasNext())
					{
						BFP2P tmp = iterator.next();
						
						if (bf.in(tmp))
						{
							rep.add(tmp);
							//***********Ajouter ce chemin dans la liste des chemins distincts qui contiennent la réponse
							// pour cette requête
							ControlerNw.search_log.get(key).addNodeMatched(this.path);
							//*******************************
						}
					}
				}
			}
		}
		
		return rep;
	}
	
	/*
	 * Rechercher le filtre précise
	 * 
	 * Retourner soit vide soit une chaîne de caractère soit un conteneur local
	 * */
	
	public Object searchExact(BFP2P bf)
	{
		ControlerNw.config_log.getTranslate().setLength(1000000);
		int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
		
		FragmentP2P f = bf.getFragment(rang);
		if (localRoute.contains(f))
		{
			Object o = localRoute.get(f);
			if (((o.getClass()).getName()).equals("java.lang.String"))
			{
				return (String)o;
			}else{
				if (((LocalContainerP2P)o).contains(bf))
				{
					//************Ajouter ce nœud dans la liste de nœuds trouvés
					ControlerNw.search_log.get(key).addNodeMatched(this.path);
					//******************************
					return ((LocalContainerP2P)o);
				}
				return null;
			}
		}
		return null;
	}
	
	/*
	 * Supprimer le filtre dans le nœud
	 * 
	 * Retourner soit vide, soit une chaîne de caractères, soit une table de routage
	 * */
	
	public Object remove(BFP2P bf)
	{
		FragmentP2P f = bf.getFragment(rang);
		if (!localRoute.contains(f))
			return null;
		
		if ((((localRoute.get(f)).getClass()).getName()).equals("java.lang.String"))
		{
			return localRoute.get(f);
		}
		else
		{
			LocalContainerP2P c = (LocalContainerP2P) localRoute.get(f);
			c.remove(bf);
			
			if(c.isEmpty())
				localRoute.remove(f);
			
			if (localRoute.isEmpty())
				return localRoute;
			return null;
		}
	}
	
	/*
	 * Supprimer l'entrée 'f' dans la table de routage
	 * 
	 * Retourner true si réussit, false sinon
	 * */
	
	public boolean remove(FragmentP2P f)
	{
		if (!localRoute.contains(f))
			return true;
		
		localRoute.remove(f);
		
		if (localRoute.isEmpty())
			return false;
		return true;
	}

	public String toString()
	{
		return "NodeID : " + path + "\n"
			 + "Rang : " + rang + "\n"
			 + localRoute.toString();
	}
}







