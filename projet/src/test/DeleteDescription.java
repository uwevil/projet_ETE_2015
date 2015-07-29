package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeleteDescription {

	public static void main(String[] args) {
		try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs (1).csv")))
		{
			reader.readLine();
			int line = 0, i = 0;
			while (true)
			{
				String s = reader.readLine();
				
				if (s == null)
					break;
				
				String[] tmp = s.split(";");
				
				if (tmp[1] != null && tmp[1] != "")
				{
					String[] tmp2 = tmp[1].split(",");
					
					if (tmp2.length >= 60)
					{
						WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs>=60_1", true);
						wf.write("//" +tmp2.length + "//;" + tmp[0] + "\n");
						wf.close();
						line++;
					}
					else if (tmp2.length > 0)
					{
						WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs<60_1", true);
						wf.write(tmp[0] + ";" + tmp[1] + "\n");
						wf.close();
						line++;
					}
					i++;
				}
			}
			
			System.out.println(" line = " + line);
			System.out.println(" i    = " + i);
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
