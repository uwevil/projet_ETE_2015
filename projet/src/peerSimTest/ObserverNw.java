package peerSimTest;

import exception.ErrorException;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;
import systeme.BF;

@SuppressWarnings("unused")
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
		if (!ok)
		{
			ok = true;
			return false;
		}
		
		Node n = Network.get(37);
		Message message = new Message();
		message.setIndexName("dcs");
		message.setSource(37);
		message.setDestinataire(37);
		
		message.setType("search");
		
		String requete = "this,list,characters,ayn,rands,novel,atlas,shrugged";
		BF bf = new BF(systeme.Configuration.sizeOfBF, 
				systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
		
		bf.addAll(requete);
		
		//String tmp = "10000001000011111011111100101101100001110000111100000001101101010000011000000110000010011000000010100000100100101001000101000101011000011101101001111000010110001100010001010000111000000000100110100111000001010100101010011010010100000100101000000000000110101001000000101011010101000100000011100111000000001101000011001000001001110010000011011101000010010010010000011001010010100010001001010100000101010010000000000000011010000110001010011010100000010100101101100001000010001001001110001000100110110000011000100001";
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
		System.out.println("Lancement de la requête : " + requete);
		EDSimulator.add(0, message, n, pid);
		
		return false;
	}

}
