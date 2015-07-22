package peerSimTest;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;
import systeme.BF;

public class ObserverNw implements Control {

	private static final String PAR_PROTOCOL = "protocol";
	private int pid;
	private boolean ok = false;
	
	public ObserverNw(String prefix)
	{
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		if(!ok)
		{
			ok = true;
			return false;
		}
		
		Node n = Network.get(65);
		Message message = new Message();
		message.setIndexName("dcs");
		message.setSource(37);
		message.setDestinataire(65);
		message.setType("search");
		
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		bf.addAll("view");
		
		Object[] o = new Object[2];
		o[0] = bf;
		o[1] = "";
		
		message.setData(o);
		
		EDSimulator.add(0, message, n, pid);
		
		return false;
	}

}
