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
		
		return false;
	}

}
