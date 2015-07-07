package systeme;

import java.util.ArrayList;

public class LocalRoute {
	private int limit;
	private ArrayList<Container<?>> localRoute;
	
	public LocalRoute(int limit) {
		// TODO Auto-generated constructor stub
		this.limit = limit;
		localRoute = new ArrayList<Container<?>>();
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public boolean add(Fragment index, Object o)
	{
		Container<BF> listBF = null;
		Container<Integer> listInt = null;
		
		if (localRoute.get(index.toInt()).isEmpty())
		{
			listBF = new Container<BF>(limit);
			listBF.add((BF)o);
			localRoute.add(listBF);
			return true;
		}
		
		if (localRoute.get(index.toInt()).getClass() == listBF.getClass())
		{
			listBF = (Container<BF>) localRoute.get(index.toInt());
			return listBF.add((BF)o);
		}else{
			listInt = (Container<Integer>) localRoute.get(index.toInt());
			return listInt.add((Integer)o);
		}
	}
	
	public Container<?> get(int index)
	{
		return localRoute.get(index);
	}

}
