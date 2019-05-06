package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import client.Packet;
import client.PacketFlag;
import model.Room;
import responders.DeleteResponder;
import responders.DownloadResponder;
import responders.FileResponder;
import responders.JoinResponder;
import responders.NewResponder;
import responders.Responder;

/*
 * Client handler receives and processes all input by the client.
 */
public class ClientHandler extends Observable implements Runnable
{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Responder responder;
	
	public ClientHandler(ObjectInputStream in, ObjectOutputStream out)
	{
		this.in = in;
		this.out = out;
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
	
	/**
	 * Receives a packet, selects a response strategy,
	 * responds to the request, and the sends the
	 * response packet back to the Client.
	 */
	public void receiveAndRespond(Packet packet)
	{
		Packet response;
		boolean notifyServer = false;
		PacketFlag flag = packet.getFlag();
		try
		{
			switch (flag)
			{
				case DELETE:
					responder = new DeleteResponder();
					notifyServer = true;
					break;
				case DOWNLOAD:
					responder = new DownloadResponder();
					break;
				case FILE:
					responder = new FileResponder();
					notifyServer = true;
					break;
				case JOIN:
					responder = new JoinResponder();
					break;
				case NEW:
					responder = new NewResponder();
					break;
				default:
					break;
			}
			response = responder.respondTo(packet);
			if (notifyServer)
			{
				updateServer(response.getRoom());
			}
			send(response);
		}
		catch (Exception e)
		{
			sendErrorMessage(e);
		}
	}
	
	/*
	 * Sends an error packet to the client.
	 */
	private void sendErrorMessage(Exception e)
	{
		Packet response = new Packet(PacketFlag.ERROR);
		response.setMessage(e.getMessage());
		send(response);
	}
	
	/*
	 * Notifies the server that a room has changed so
	 * that the server can notify all other client handlers.
	 */
	private void updateServer(Room room)
	{
		setChanged();
		notifyObservers(room);
	}
	
	/*
	 * Updates the client with the specified room. If the
	 * Client is not in this room the packet will be disregarded,
	 * but if they are, then it allows for near-instantaneous updates
	 * for all Clients in that room.
	 */
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
