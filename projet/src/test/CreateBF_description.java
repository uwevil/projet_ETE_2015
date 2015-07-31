package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CreateBF_description {
	public static void main (String[]args)
	{
		System.out.println("Commence");
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs<60_description_only", false);
		int line = 0;
		try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60")))
		{	
			while (true)
			{
				String s = new String();
				s = reader.readLine();
				if (s == null)
					break;
				String[] tmp = s.split(";");
				
				if (tmp.length >= 2 && tmp[1].length() > 2 )
				{	
					wf.write(tmp[1] + "\n");
					line++;
				}
			}
			wf.close();
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("End " + line);
	}
}
