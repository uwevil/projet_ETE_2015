package peerSimTest;

import peersim.config.Configuration;
import peersim.core.Control;

public class ObserverNw implements Control {

	private static final String PAR_PROTOCOL = "protocol";
	@SuppressWarnings("unused")
	private int pid;
	
	public ObserverNw(String prefix)
	{
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		/*
		Node n = Network.get(37);
		Message message = new Message();
		message.setIndexName("dcs");
		message.setSource(0);
		message.setDestinataire(0);
		message.setType("removeIndex");
		
		EDSimulator.add(0, message, n, pid);
		*/
		
		return false;
	}

}
