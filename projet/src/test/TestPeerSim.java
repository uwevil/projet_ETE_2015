package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import exception.ErrorException;
import peerSimTest.BFP2P;
import peerSimTest.Config;

public class TestPeerSim {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws ErrorException {
		System.out.println("Commence");
		Config config_log = new Config();

		int line = 0;

		try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60_BF")))
		{	
			BFP2P bf = new BFP2P(config_log.sizeOfBF, 
					config_log.sizeOfBF/config_log.numberOfFragment);
			
			bf.add("view");
			
			while (true)
			{
				String s = new String();
				s = reader.readLine();
				if (s == null)
					break;
							
				BFP2P bf_tmp = new BFP2P(s, config_log.sizeOfBF/config_log.numberOfFragment);
				
				if (bf.in(bf_tmp))
				{
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/wikiDocs<60_BF_test", false);
					wf.write(bf_tmp + "\n");
					wf.close();
				}
				line++;
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("End " + line);
	}

}
