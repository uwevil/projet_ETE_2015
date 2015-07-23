package peerSimTest;

import java.io.File;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class CreateNw implements Control {
	
	public static final String PAR_PROTOCOL = "protocol";
	
	@SuppressWarnings("unused")
	private String prefix;
	private int pid;
	
	public CreateNw(String prefix) {
		// TODO Auto-generated constructor stub
		this.prefix = prefix;
		pid = Configuration.getPid(prefix + "." + PAR_PROTOCOL);
	}
	

	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		for (int i = 0; i < Network.size(); i++)
		{
			Node n = (Node) Network.get(i);
			SystemIndexProtocol s = (SystemIndexProtocol) n.getProtocol(pid);
			s.setNodeIndex(i);
		}
		
		File f = new File(systeme.Configuration.peerSimLOG);
		if (f.exists())
			f.delete();
		f = new File(systeme.Configuration.peerSimLOG_resultat);
		if (f.exists())
			f.delete();
		f = new File(systeme.Configuration.peerSimLOG_path);
		if (f.exists())
			f.delete();
		
		return false;
	}

}
