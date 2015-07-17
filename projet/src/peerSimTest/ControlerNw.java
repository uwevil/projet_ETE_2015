package peerSimTest;

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
	
	public ControlerNw(String prefix)
	{
		this.prefix = prefix;
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		Node n = Network.get(0);
		Message message = new Message();
		message.setType("createIndex");
		message.setData1("dcs");
		message.setData2(0);
		message.setData3(0);
		
		EDSimulator.add(0, message, n, pid);
		
		return false;
	}

}
