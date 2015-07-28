package peerSimTest;

import java.io.FileNotFoundException;
import java.util.Enumeration;

import exception.ErrorException;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;
import test.ReadFile;
import test.WriteFile;

@SuppressWarnings("unused")
public class ObserverNw implements Control {

	private static final String PAR_PROTOCOL = "protocol";
	private int pid;
	private boolean ok = true, ok2 = false, ok3 = false;
	
	public ObserverNw(String prefix)
	{
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		if (ok)
		{
			ok = false;
			ok2 = true;
			return false;
		}
		else if (ok2)
		{
	
			Node n = Network.get(37);
	/*		
			Message message = new Message();
			message.setIndexName("dcs");
			message.setSource(37);
			message.setDestinataire(37);
			
			message.setType("search");
			
			String requete = "view";
			BF bf = new BF(systeme.Configuration.sizeOfBF, 
					systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
			
			bf.addAll(requete);
			
			Object[] o = new Object[2];
			o[0] = bf;
			o[1] = "";
			
			message.setData(o);
			System.out.println("Lancement de la requête : " + requete);
			EDSimulator.add(0, message, n, pid);
			
			
			/*
			String tmp = "10000001000011111011111100101101100001110000111100000001101101010000011000000110000010011000000010100000100100101001000101000101011000011101101001111000010110001100010001010000111000000000100110100111000001010100101010011010010100000100101000000000000110101001000000101011010101000100000011100111000000001101000011001000001001110010000011011101000010010010010000011001010010100010001001010100000101010010000000000000011010000110001010011010100000010100101101100001000010001001001110001000100110110000011000100001";
			BF bf1 = null;
			try {
				bf1 = new BF(tmp, systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
			} catch (ErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Message message = new Message();
			message = new Message();
			message.setIndexName("dcs");
			message.setSource(37);
			message.setDestinataire(37);
			
			message.setType("searchExact");
			
			Object[] o = new Object[2];
			o[0] = bf1;
			o[1] = "";
			
			message.setData(o);
			System.out.println("Lancement de la requête exact : " + tmp);
			EDSimulator.add(0, message, n, pid);
			
			Message message1 = new Message();
			message1 = new Message();
			message1.setIndexName("dcs");
			message1.setSource(37);
			message1.setDestinataire(37);
			
			message1.setType("remove");
			
			message1.setData(bf1);
			message1.setPath("");
			
			System.out.println("Lancement de la suppression exact : " + tmp);
			EDSimulator.add(0, message1, n, pid);
			
			System.out.println("Lancement de la requête exact : " + tmp);
			EDSimulator.add(0, message, n, pid);
	*/
			
			try {
				ReadFile rf = new ReadFile("/Users/dcs/vrac/test/wikiDocs<60_500_request");
				
				for (int i = 0; i < 1; i++) //rf.size(); i++)
				{
					Message message = new Message();
					message.setIndexName("dcs");
					message.setSource(37);
					message.setDestinataire(37);
					
					message.setType("search");
					
					BFP2P bf = new BFP2P(systeme.Configuration.sizeOfBF, 
							systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
					
					bf.addAll(rf.getDescription(i));
					
					Object[] o = new Object[2];
					o[0] = bf;
					o[1] = "";
					
					message.setData(o);
					
					EDSimulator.add(0, message, n, pid);
				}
				System.out.println("NOMBRE de requete = " + rf.size());
			} 
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ok2 = false;
		//	ok3 = true;
		}
		
		if (ok3)
		{
			//*******************
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG+"_indexHeight", false);
			Enumeration<Integer> enumeration = systeme.Configuration.indexHeight.keys();
			
			while (enumeration.hasMoreElements())
			{
				Integer i = enumeration.nextElement();
				if (i <= 9)
				{
					wf.write(i + "  " + systeme.Configuration.indexHeight.get(i) + "\n");
				}
				else
				{
					wf.write(i + " " + systeme.Configuration.indexHeight.get(i) + "\n");
				}
			}
			
			wf.close();
			//*******************
			
			ok3 = false;
		}
		
		return false;
	}

}
