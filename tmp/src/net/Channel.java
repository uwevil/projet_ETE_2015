package net;

public class Channel {
	private Object object = null;
	
	public Channel() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void send(Object object)
	{
		while (hasMessage())
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		this.object = object;
		notify();
	}
	
	public synchronized Object receive()
	{
		while (!hasMessage())
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Object tmp = object;
		this.object = null;
		notify();
		return tmp;
	}
	
	private boolean hasMessage()
	{
		if (object == null)
			return false;
		return true;
	}

}
