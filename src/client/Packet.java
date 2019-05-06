package client;

import java.io.Serializable;

import model.Room;

public class Packet implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final PacketFlag flag;
	private String message;
	private String fileName;
	private byte[] fileContents;
	private Room room;
	
	public Packet(PacketFlag flag)
	{
		this.flag = flag;
	}
	
	public PacketFlag getFlag()
	{
		return flag;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public byte[] getFileContents()
	{
		return fileContents;
	}
	
	public void setFileContents(byte[] fileContents)
	{
		this.fileContents = fileContents;
	}
	
	public Room getRoom()
	{
		return room;
	}
	
	public void setRoom(Room room)
	{
		this.room = room;
	}
}
