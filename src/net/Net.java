package net;

import java.util.ArrayList;

public class Net {
	private ArrayList<Channel> list = new ArrayList<Channel>();
	
	public Net() {
		// TODO Auto-generated constructor stub
	}
	
	public void send(Object o, int index)
	{
		list.get(index).send(o);
	}
	
	public Object receive(int index)
	{
		return list.get(index).receive();
	}
	
	public void deleteChannel(int index)
	{
		list.remove(index);
	}
	
	public int addChannel()
	{
		Channel tmp = new Channel();
		list.add(tmp);
		return list.lastIndexOf(tmp);
	}

}
