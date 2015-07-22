package peerSimTest;

import java.io.BufferedReader;
import java.io.File;
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
	private int line = 0;
	private boolean ok = true, ok2 = true;
	
	public ControlerNw(String prefix)
	{
		this.prefix = prefix;
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
		File f = new File("/Users/dcs/vrac/test/21-07-peersim_log");
		if (f.exists())
			f.delete();
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		/*
		Node n = Network.get(23);
		Message message = new Message();
		message.setType("createIndex");
		message.setIndexName("dcs");
		message.setSource(0);
		message.setDestinataire(23);
		
		EDSimulator.add(0, message, n, pid);
		/*
		if (times % 2 != 0)
		{
			message.setType("removeIndex");
			message.setIndexName("dcs" + (times++ - 1));
			EDSimulator.add(0, message, n, pid);
		}
		else
		{
			message.setType("createIndex");
			message.setIndexName("dcs" + times++);
			EDSimulator.add(0, message, n, pid);
		}
		*/
		Node n; 
		Message message = new Message();

		if (ok && ok2)
		{
			 n = Network.get(23);
			 message.setType("createIndex");
			 message.setIndexName("dcs");
			 message.setSource(23);
			 message.setDestinataire(23);
		
			 ok = false;
			 EDSimulator.add(0, message, n, pid);
		}
		else if (ok2)
		{
			n = Network.get(23);
			//System.out.println("******Commence******");
			//long time = System.currentTimeMillis();
			
			try(BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60_aa")))
			{
				String s;
				int j = 0;
				while ((s = reader.readLine()) != null)
				{
					String[] tmp = s.split(";");
					
					if (tmp.length >= 2 && tmp[1].length() > 3 && line == j)
					{
						BF bf = new BF(systeme.Configuration.sizeOfBF, 
								systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
						bf.addAll(tmp[1]);
						
						message.setType("add");
						message.setIndexName("dcs");
						message.setPath("");
						message.setData(bf);
						message.setSource(37);
						message.setDestinataire(23);
					
						EDSimulator.add(0, message, n, pid);
						line++;
						break;
					}
					j++;
				}
				reader.close();
				//if (s == null)
				if (line == 100)	
					ok2 = false;
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("Fini la lecture : " 
		//		+(System.currentTimeMillis() - time)/(1000) + " s");
		
		return false;
	}

}
