package simulateur;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OpenConnection {
	private ServerSocket socket;
	
	public OpenConnection() {
		// TODO Auto-generated constructor stub
		try {
			socket = new ServerSocket(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ServerSocket getServerSocket()
	{
		return this.socket;
	}

	public static void main(String[] args)
	{
		OpenConnection openConnection = new OpenConnection();
		Socket client = null;
		while (true)
		{
			ServerSocket serveur = openConnection.getServerSocket();
			try {
				client = serveur.accept();
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				
				System.out.println((String) in.readObject());
				out.writeObject((Object) new String("ca marche"));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					client.close();
					serveur.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}
