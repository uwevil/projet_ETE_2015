package systeme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import test.WriteFile;

public class GenerationRequest {
	
	public static int numberOfRequestPerFile = 10;
	public static int numberOfWord = 5;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60")))
		{
			String s;
			int numberOfRequest = 0;
			WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs<60_500_request", false);

			while (numberOfRequest < 500)
			{
				for (int i = 0; i < numberOfRequestPerFile; i++)
				{
					while ((s = reader.readLine()) != null)
					{
						String[] tmp = s.split(";");
						String[] tmp1 = tmp[1].split(",");
						
						if (tmp.length >= 2 && tmp1.length >= numberOfWord)
						{
							wf.write(tmp[0]+ ";");
							for (int j = 0; j < numberOfWord; j++)
							{
								wf.write(tmp1[j]);
								if (j < numberOfWord - 1)
									wf.write(",");
							}
							wf.write("\n");
							break;
						}
					}
				}
				numberOfRequest += numberOfRequestPerFile;
				numberOfWord++;
			}
			
			wf.close();			
			reader.close();
			
			System.out.println("Nombre de requêtes = " + numberOfRequest);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
