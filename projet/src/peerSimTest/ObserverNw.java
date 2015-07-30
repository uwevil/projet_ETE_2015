package peerSimTest;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

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
	private boolean ok = true, ok2 = false;
	private int experience = 0;
	
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
		else if (ok2 && ControlerNw.config_log.getExperience_OK() && Config.ObserverNw_OK)
		{
			Node n = Network.get(37);
			
			System.out.println("Expérience n° " + experience);
			/*
			try 
			{
				ReadFile rf = new ReadFile("/Users/dcs/vrac/test/wikiDocs<60_500_request");
				
				int j = 0;
				
				String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
				Config.peerSimLOG = "/Users/dcs/vrac/test/"+ date + "/" + experience + "_log";
				Config.peerSimLOG_resultat = "/Users/dcs/vrac/test/" + date + "/" + experience + "_resultat_log";
				Config.peerSimLOG_path = "/Users/dcs/vrac/test/" + date + "/" + experience + "_path_log";
		
				for (int i = experience*10; i < rf.size() && j < 10; i++)
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
					o[1] = "/";
					
					message.setData(o);
					j++;
					EDSimulator.add(0, message, n, pid);
				}
				experience++;
				
				System.out.println("NOMBRE de requete = " + rf.size());
			} 
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ControlerNw.config_log.setExperience_OK(false);
			
			if (experience*10 >= 500)
			{
				ok2 = false;
			}
			*/
			
			Message message = new Message();
			message.setIndexName("dcs");
			message.setSource(37);
			message.setDestinataire(37);
			
			message.setType("search");
			
			BFP2P bf = new BFP2P(systeme.Configuration.sizeOfBF, 
					systeme.Configuration.sizeOfBF/systeme.Configuration.numberOfFragment);
			
			bf.addAll("view");
			
			Object[] o = new Object[2];
			o[0] = bf;
			o[1] = "/";
			
			message.setData(o);
			EDSimulator.add(0, message, n, pid);
			ok2 = false;
			ControlerNw.config_log.setExperience_OK(false);
		}
		
		if (ControlerNw.config_log.getEnd_OK())
		{
			/*
			//*******************
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG+"_indexHeight", false);
			Enumeration<Integer> enumeration = ControlerNw.config_log.getIndexHeight().keys();
			
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
			*/
			
			
			WriteFile wf = new WriteFile(systeme.Configuration.peerSimLOG+"_time", false);
			wf.write("RequeteID temps(ms)\n");
			
			Enumeration<Integer> enumeration = ControlerNw.config_log.getTimeGlobal().keys();
			long time = 0;
			int size = 0;
			
			while (enumeration.hasMoreElements())
			{
				Integer i = enumeration.nextElement();
				
				long tmp = ControlerNw.config_log.getTimeGlobal().get(i);
				time += tmp;
				if (i.toString().length() == 5)
				{
					wf.write(i + "   " + tmp + " ms\n");
				}
				else if (i.toString().length() == 4)
				{
					wf.write(i + "    " + tmp + " ms\n");
				}
				else if (i.toString().length() == 6)
				{
					wf.write(i + "  " + tmp + " ms\n");
				}
				size++;
			}
			
			long i = time/size;
			long hours = TimeUnit.MILLISECONDS.toHours(i);
			i -= TimeUnit.HOURS.toMillis(hours);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(i);
			i -= TimeUnit.MINUTES.toMillis(minutes);
			long seconds = TimeUnit.MILLISECONDS.toSeconds(i);
			i -= TimeUnit.SECONDS.toMillis(seconds);
			
			wf.write("\n"
					+ "Temps total        : " + time + " ms\n"
					+ "Nombre de requetes : " + size + " requêtes\n"
					+ "Temps moyen        : " + time/size + " ms == "
					+ hours + " h " + minutes + " m " + seconds + " s " + i + " ms"
					+ "\n\n");
			
			wf.close();
			
			System.out.println("setExpérience_OK = true");
			ControlerNw.config_log.setEnd_OK(false);
			ControlerNw.config_log.setExperience_OK(true);

		}
		
		return false;
	}

}
