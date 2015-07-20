package test;

import java.io.IOException;

public class DeleteDescription {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadFile r2;
		try {
			r2 = new ReadFile("/Users/dcs/vrac/test/bf_cab.csv");
			System.out.println(r2.size() + " données lues");
						
			for (int i = 0; i < r2.size(); i++)
			{
				String s = r2.getDescription(i);
				
				String[] tmp2 = s.split(",");
				if (tmp2.length >= 60)
				{
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/bf_caf>60", true);
					wf.write("//" +tmp2.length + "//;" + r2.getdocUrl(i) + "\n");
					wf.close();
				}
				/*
				else
				{
					WriteFile wf = new WriteFile("/Users/dcs/vrac/test/bf_caf<60", true);
					wf.write(r2.getdocUrl(i) + ";" + r2.getDescription(i) + "\n");
					wf.close();
				}
				*/
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("end");
	}

}
