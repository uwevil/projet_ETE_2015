package peerSimTest;

import java.io.File;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;

public class ControlerNw implements Control {
	private static final String PAR_PROTOCOL = "protocol";

	@SuppressWarnings("unused")
	private String prefix;
	private int pid;
	private boolean ok = true, ok2 = true;
	
	public ControlerNw(String prefix)
	{
		this.prefix = prefix;
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
		File f = new File(systeme.Configuration.peerSimLOG);
		if (f.exists())
			f.delete();
		f = new File(systeme.Configuration.peerSimLOG_resultat);
		if (f.exists())
			f.delete();
		f = new File(systeme.Configuration.peerSimLOG_path);
		if (f.exists())
			f.delete();
		
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		
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
			message.setType("init");
			
			EDSimulator.add(0, message, Network.get(10), pid);
			ok2 = false;
		}
			
		return false;
	}

}
