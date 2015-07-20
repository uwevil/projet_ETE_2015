package systeme;

public class CalculRang {
	
	public CalculRang() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public int getRang(String path)
	{
		if (path == "")
			return 0;
		
		String[] tmp = path.split("/");
		
		return tmp.length - 1;
	}

}
