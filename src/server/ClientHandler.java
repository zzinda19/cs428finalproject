package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import client.Packet;
import client.PacketFlag;
import model.Room;
import utility.CorruptedPacketException;
import utility.InvalidRoomCodeException;
import utility.InvalidRoomNameException;

public class ClientHandler extends Observable implements Runnable
{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private RoomDatabase database;
	
	public ClientHandler(ObjectInputStream in, ObjectOutputStream out)
	{
		this.in = in;
		this.out = out;
		this.database = RoomDatabase.getDatabase();
	}
	
	public void run()
	{
		Packet packet;
		while (true)
		{
			try
			{
				packet = (Packet) in.readUnshared();
				receiveAndRespond(packet);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
		close();
	}
	
	public void send(Packet packet)
	{
		try
		{
			out.reset();
			out.writeUnshared(packet);
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void receiveAndRespond(Packet packet)
	{
		Packet response;
		Room responseRoom;
		byte[] responseFile;
		PacketFlag flag = packet.getFlag();
		try
		{
			switch (flag)
			{
				case DELETE:
					response = new Packet(PacketFlag.ROOM);
					responseRoom = deleteFile(packet);
					response.setRoom(responseRoom);
					setChanged();
					notifyObservers(responseRoom);
					send(response);
					break;
				case DOWNLOAD:
					response = new Packet(PacketFlag.FILE);
					responseFile = getFile(packet);
					response.setFileContents(responseFile);
					response.setFileName(packet.getFileName());
					send(response);
					break;
				case FILE:
					response = new Packet(PacketFlag.ROOM);
					responseRoom = addFileToRoom(packet);
					response.setRoom(responseRoom);
					setChanged();
					notifyObservers(responseRoom);
					send(response);
					break;
				case JOIN:
					response = new Packet(PacketFlag.ROOM);
					responseRoom = joinRoom(packet);
					response.setRoom(responseRoom);
					send(response);
					break;
				case NEW:
					response = new Packet(PacketFlag.ROOM);
					responseRoom = newRoom(packet);
					response.setRoom(responseRoom);
					send(response);
					break;
				default:
					break;
			}
		}
		catch (Exception e)
		{
			sendErrorMessage(e);
		}
	}
	
	private Room addFileToRoom(Packet packet) throws Exception
	{
		if (packet.getFlag() != PacketFlag.FILE) throw new CorruptedPacketException();
		String code = packet.getMessage();
		if (database.isValidCode(code) == false) throw new InvalidRoomCodeException();
		database.addFileToRoom(code, packet.getFileName(), packet.getFileContents());
		return database.getRoom(code);
	}
	
	private Room joinRoom(Packet packet) throws Exception
	{
		if (packet.getFlag() != PacketFlag.JOIN) throw new CorruptedPacketException();
		String code = packet.getMessage();
		if (database.isValidCode(code) == false) throw new InvalidRoomCodeException();
		return database.joinRoom(code);
	}
	
	private Room newRoom(Packet packet) throws Exception
	{
		if (packet.getFlag() != PacketFlag.NEW) throw new CorruptedPacketException();
		String name = packet.getMessage();
		if (name.isEmpty()) throw new InvalidRoomNameException();
		return database.newRoom(name);
	}
	
	private byte[] getFile(Packet packet) throws Exception
	{
		if (packet.getFlag() != PacketFlag.DOWNLOAD) throw new CorruptedPacketException();
		String code = packet.getMessage();
		if (database.isValidCode(code) == false) throw new InvalidRoomCodeException();
		String fileName = packet.getFileName();
		return database.getFileForRoom(code, fileName);
	}
	
	private Room deleteFile(Packet packet) throws Exception
	{
		if (packet.getFlag() != PacketFlag.DELETE) throw new CorruptedPacketException();
		String code = packet.getMessage();
		if (database.isValidCode(code) == false) throw new InvalidRoomCodeException();
		String fileName = packet.getFileName();
		return database.deleteFileForRoom(code, fileName);
	}
	
	private void sendErrorMessage(Exception e)
	{
		Packet response = new Packet(PacketFlag.ERROR);
		response.setMessage(e.getMessage());
		send(response);
	}
	
	public void updateClient(Room room)
	{
		Packet response = new Packet(PacketFlag.ROOM);
		response.setRoom(room);
		send(response);
	}
	
	private void close()
	{
		try
		{
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
