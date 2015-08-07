package peerSimTest_v1_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import test.ReadFile;
import test.WriteFile;
import exception.ErrorException;

public class TestValidate {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws ErrorException {
		String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
		Config config_log = new Config();
		int experience = 0;
		try
		{	
			ReadFile rf = new ReadFile("/Users/dcs/vrac/test/wikiDocs<60_500_request");
			
			
			int essai = 0;
			date = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
			while (experience*10 < 500)
			{
				System.out.println("Begin experience n° " + experience);
				
				Config.peerSimLOG = "/Users/dcs/vrac/test/"+ date 
						+ "/Essai" + essai + "/" + experience + "_log";
				Config.peerSimLOG_resultat = "/Users/dcs/vrac/test/" + date 
						+ "/Essai" + essai + "/" + experience + "_resultat_log";
				Config.peerSimLOG_path = "/Users/dcs/vrac/test/" + date 
						+ "/Essai" + essai + "/" + experience + "_path_log";
				
				int j = 0;

				for (int i = experience*10; i < rf.size() && j < 10; i++)
				{
					///////////
					BFP2P bf = new BFP2P(systeme.Configuration.sizeOfBF, 
							config_log.sizeOfBF/config_log.numberOfFragment);
					
					bf.addAll(rf.getDescription(i));
					//////////
					
					ControlerNw.config_log.getTranslate().setLength(1000000);
					int key = ControlerNw.config_log.getTranslate().translate(bf.toString());
					
					File f = new File(Config.peerSimLOG_resultat + "_"+key);
					if (f.exists())
					{
						BufferedReader reader1 = new BufferedReader(new FileReader(Config.peerSimLOG_resultat + "_"+key));
						
						while (true)
						{
							////////////
							String requete = reader1.readLine();
							
							if (requete == null)
								break;
							
							BFP2P bf1 = new BFP2P(requete, 
									config_log.sizeOfBF/config_log.numberOfFragment);
							////////////
														
							if (!bf.in(bf1))
							{	
								WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date 
										+ "/Essai" + (essai + 1) 
										+ "/" + experience + "_testValidate_fail_" 
										+ key, true);
								
								wf.write(bf + ";"+ rf.getDescription(i) +"\n");
								wf.write(bf1 + "\n\n");
								
								wf.close();
								
								continue;
							}
						}
						reader1.close();
					}
					
					BufferedReader reader = new BufferedReader(
							new FileReader("/Users/dcs/vrac/test/wikiDocs<60_BF_only"));
				
					while (true)
					{
						String s = reader.readLine();
						
						if (s == null)
							break;
						
						///////////////
						BFP2P bf2 = new BFP2P(s, 
								config_log.sizeOfBF/config_log.numberOfFragment);
						///////////////
						
						if (bf.in(bf2))
						{
							WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date 
									+ "/Essai" + (essai + 1) 
									+ "/" + experience + "_testValidate_" + key, true);
							wf.write(s + "\n");
							wf.close();
						}
					}
					reader.close();
					j++;
				}
				System.out.println("End experience n° " + experience);

				experience++;
			}
				
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("End after " + experience + " experiences");
	}

}
