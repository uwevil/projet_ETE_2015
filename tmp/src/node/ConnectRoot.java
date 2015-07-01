package node;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectRoot {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public ConnectRoot() {
		// TODO Auto-generated constructor stub
		try {
			socket = new Socket("localhost", 9632);
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendRoot(Object o)
	{
		try {
			out.writeObject(o);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object receiveRoot()
	{
		Object tmp = null;
		try {
			tmp = in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}
	
	public void closeRoot()
	{
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void main(String[] args)
	{
		ConnectRoot connectRoot = new ConnectRoot();
		connectRoot.sendRoot((Object) new String("T'as recu???"));
		System.out.println((String)connectRoot.receiveRoot());
		connectRoot.closeRoot();
	}

}
