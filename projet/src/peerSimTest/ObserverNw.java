package peerSimTest;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;

public class ObserverNw implements Control {

	private static final String PAR_PROTOCOL = "protocol";
	private int pid;
	
	public ObserverNw(String prefix)
	{
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		Node n = Network.get(30);
		Message message = new Message();
		message.setData("dcs");
		message.setSource(0);
		message.setDestinataire(0);
		message.setType("removeIndex");
		
		EDSimulator.add(0, message, n, pid);
		return false;
	}

}
