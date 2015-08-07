package peerSimTest_v1_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import serveur.Message;
import test.WriteFile;

public class TestRemove implements Control {
	private static final String PAR_PROTOCOL = "protocol";
	private int pid;
	private boolean ok = true, ok2 = false, ok3 = false;
	private int line = 0;
	
	public TestRemove(String prefix)
	{
		pid = Configuration.getPid(prefix+ "." + PAR_PROTOCOL);
	}
	
	@Override
	public boolean execute() {
		
		if (ok)
		{
			ok = false;
			ok2 = true;
			return false;
		}
		else if (ok2 && Config.ObserverNw_OK )
		{
			Node n = Network.get(37);
			
			System.out.println("Begin remove");
			
			try 
			{								
				n = Network.get(23);
				BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60"));
				
				while (true)
				{
					String s = new String();
					s = reader.readLine();
					if (s == null)
						break;
					String[] tmp = s.split(";");
					
					if (tmp.length >= 2 && tmp[1].length() > 2 )
					{
						BFP2P bf_tmp = new BFP2P(Config.sizeOfBF, 
								Config.sizeOfBF/Config.numberOfFragment);
						bf_tmp.addAll(tmp[1]);

						Message message = new Message();

						message.setType("remove");
						message.setIndexName("dcs");
						message.setPath("/");
						message.setData(bf_tmp);
						message.setDestinataire(23);
						line++;

						EDSimulator.add(0, message, n, pid);
					}
					
					if (line == 2000000)
						break;
				}
				reader.close();
				ok2 = false;
				ok3 = true;
				System.out.println("Fini de remove " + line + " lignes");	
			}	
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (ok3)
		{
			Node n = Network.get(37);
			
			System.out.println("Begin search Exact");
			
			try 
			{		
				String essai = "ExactAll";
				String date = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
				Config.peerSimLOG = "/Users/dcs/vrac/test/"+ date + "/Essai" + essai + "/" + "_log";
				Config.peerSimLOG_resultat = "/Users/dcs/vrac/test/" + date + "/Essai" + essai + "/" + "_resultat_log";
				Config.peerSimLOG_path = "/Users/dcs/vrac/test/" + date + "/Essai" + essai + "/" + "_path_log";
				
				BufferedReader reader = new BufferedReader(new FileReader("/Users/dcs/vrac/test/wikiDocs<60"));
				
				int i = 0;
				while (true)
				{
					String s = new String();
					s = reader.readLine();
					if (s == null)
						break;
					
					if (i < 3386487)
					{
						i++;
						continue;
					}
					else
					{
						String[] tmp = s.split(";");
						
						if (tmp.length >= 2 && tmp[1].length() > 2 )
						{
							BFP2P bf_tmp = new BFP2P(Config.sizeOfBF, 
									Config.sizeOfBF/Config.numberOfFragment);
							bf_tmp.addAll(tmp[1]);

							Message message = new Message();

							Object[] o = new Object[2];
							o[0] = bf_tmp;
							o[1] = "/";
							
							message.setType("searchExact");
							message.setIndexName("dcs");
							message.setData(o);
							message.setSource(37);
							message.setDestinataire(37);
							EDSimulator.add(0, message, n, pid);
						}
					}
					i++;
					
				//	if (i == 1600)
					//	break;
				}
				reader.close();
				ok3 = false;
								
				System.out.println("Fini de searchExact " + line + " lignes");	
			}	
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (ControlerNw.config_log.getEnd_OK())
		{	
			WriteFile wf = new WriteFile(Config.peerSimLOG+"_time", false);
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
			
			ControlerNw.config_log.setEnd_OK(false);
		}
		return false;
	}
}
