package peerSimTest;

import exception.ErrorException;
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
	
	public ObserverNw(String prefix)
	{
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
	
		Node n = Network.get(65);
		Message message = new Message();
		message.setIndexName("dcs");
		message.setSource(37);
		message.setDestinataire(65);
		message.setType("search");
		
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		bf.addAll("view");
		
		//String tmp = "11100100100010000100000000010010111000001010100101010000010010100000010000001001110100000000000101001110001000000110010100100010010001101101010110111000110001000010001100101000001100000001011000100101100010001001010110101010010100000000000011101000000110001000111000000000011000001000100000010011011111000100100010011001101010010010000110011000010000001000000000000010100110011010000001011000100001000011001110000100000001000100000000001110001000101000001010100010000000000001000001000000010100000100011000110100";
		//BF bf = null;
		/*try {
			bf = new BF(tmp, systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		Object[] o = new Object[2];
		o[0] = bf;
		o[1] = "";
		
		message.setData(o);
		
		EDSimulator.add(0, message, n, pid);
		
		return false;
	}

}
