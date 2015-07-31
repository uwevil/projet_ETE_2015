package peerSimTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import exception.ErrorException;
import test.ReadFile;
import test.WriteFile;

public class TestMatched {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws ErrorException {
		String date = (new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss")).format(new Date());
		Config config_log = new Config();
		int experience = 0;
		try
		{	
			ReadFile rf = new ReadFile("/Users/dcs/vrac/test/wikiDocs<60_500_request");	
			
			int essai = 0;
			date = "30-07-2015"; //(new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
			
			ArrayList<String> description = new ArrayList<String>();
			
			BufferedReader reader = new BufferedReader(
					new FileReader("/Users/dcs/vrac/test/wikiDocs<60_description_only"));
			
			int k = 0;
			while (true)
			{
				String s = reader.readLine();
				
				if (s == null)
					break;
				
				description.add(s);
				k++;
			}
			reader.close();
			
			System.out.println(k);
			
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
					
					File f = new File("/Users/dcs/vrac/test/" + date 
							+ "/Essai" + (essai + 1) 
							+ "/" + experience + "_testValidate_" + key);
					if (f.exists())
					{
						BufferedReader reader1 = new BufferedReader(
								new FileReader("/Users/dcs/vrac/test/" + date 
								+ "/Essai" + (essai + 1) 
								+ "/" + experience + "_testValidate_" + key));
						
						ArrayList<BFP2P> tabBF = new ArrayList<BFP2P>();
						
						while (true)
						{
							////////////
							String requete = reader1.readLine();
							
							if (requete == null)
								break;
							
							BFP2P bf1 = new BFP2P(requete, 
									config_log.sizeOfBF/config_log.numberOfFragment);
							////////////
							
							tabBF.add(bf1);
							
						}
						reader1.close();
						
						for (k = 0; k < description.size(); k++)
						{
							String s = description.get(k);
							
							if (s == null)
								break;
							
							///////////////
							BFP2P bf2 = new BFP2P(config_log.sizeOfBF, 
									config_log.sizeOfBF/config_log.numberOfFragment);
							
							bf2.addAll(s);
							///////////////
							
							for (int m = 0; m < tabBF.size(); m++)
							{
								BFP2P bf_tmp = tabBF.get(m);
								
								if (bf_tmp.equals(bf2))
								{
									WriteFile wf = new WriteFile("/Users/dcs/vrac/test/" + date 
											+ "/Essai" + (essai + 2) 
											+ "/" + experience + "_testValidate_" + key, true);
									wf.write(rf.getDescription(i) + ";" + s + "\n");
									wf.close();
								}
							}
							System.out.println(k);
						}
					}
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
