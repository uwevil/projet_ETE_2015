package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
	private FileWriter fw;
	
	public WriteFile(String file, boolean append) {
		// TODO Auto-generated constructor stub
		try {
			fw = new FileWriter(new File(file), append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String s)
	{
		try {
			fw.write(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}