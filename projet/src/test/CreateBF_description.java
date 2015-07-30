package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import peerSimTest.BFP2P;
import peerSimTest.Config;

public class CreateBF_description {
	public static void main (String[]args)
	{
		System.out.println("Commence");
		Config config_log = new Config();
		WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs<60_BF", false);
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
					@SuppressWarnings("static-access")
					BFP2P bf_tmp = new BFP2P(config_log.sizeOfBF, 
							config_log.sizeOfBF/config_log.numberOfFragment);
					
					bf_tmp.addAll(tmp[1]);
					
					wf.write(bf_tmp.toString() + "\n");
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
