package serveur;

import java.util.ArrayList;

import projet.BF;
import projet.Fragment;

public class LocalRoute {
	private int limit;
	private ArrayList<ArrayList<BF>> localRoute;
	public LocalRoute(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		localRoute = new ArrayList<ArrayList<BF>>();
	}
	
	public boolean add(Fragment index, BF bf)
	{
		ArrayList<BF> list = localRoute.get(index.toInt());
		
		if (list.isEmpty())
		{
			list = new ArrayList<BF>();
			list.add(bf);
			localRoute.add(list);
			return true;
		}
			
		if (list.size() == limit)
			return false;
		
		list.add(bf);
		
		return true;
	}

}
