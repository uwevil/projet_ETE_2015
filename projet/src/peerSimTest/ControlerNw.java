package peerSimTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;
import systeme.BF;

public class ControlerNw implements Control {
	private static final String PAR_PROTOCOL = "protocol";

	@SuppressWarnings("unused")
	private String prefix;
	private int pid;
	private boolean ok = true, ok2 = true;
	private int line = 0;
	
	public ControlerNw(String prefix)
	{
		this.prefix = prefix;
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		
		Node n; 

		if (ok && ok2)
		{
			Message message = new Message();

			 n = Network.get(23);
			 message.setType("createIndex");
			 message.setIndexName("dcs");
			 message.setSource(23);
			 message.setDestinataire(23);
		
			 ok = false;
			 EDSimulator.add(0, message, n, pid);
		}
		else if (ok2 && line <= 30000)
		{
			n = Network.get(23);
			try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60_aa")))
			{
				while (true)
				{
					String s = new String();
					s = reader.readLine();
					if (s == null)
						break;
					String[] tmp = s.split(";");
					
					if (tmp.length >= 2 && tmp[1].length() > 3 )
					{
						BF bf_tmp = new BF(systeme.Configuration.sizeOfBF, 
								systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
						bf_tmp.addAll(tmp[1]);
						//bf_tmp.add("" + line);
						Message message = new Message();

						message.setType("add");
						message.setIndexName("dcs");
						message.setPath("");
						message.setData(bf_tmp);
						message.setDestinataire(23);
						
						EDSimulator.add(0, message, n, pid);
						line++;
						if (line > 30000)
							break;
					}
				}
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
			
		return false;
	}

}
