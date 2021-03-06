package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Room;

/*
 * Centralized server class handles all connections from clients
 * and spawns a ClientHandler thread for each. Implements the observer
 * class to notify all Clients when a room is updated, allowing for
 * real-time updates to every Client in a room when one Client adds
 * or deletes a file.
 */
public class Server extends Thread implements Observer
{
	private static final int port = 6112;
	private ServerSocket serverSocket;
	private List<ClientHandler> handlers;
	
	public Server()
	{
		handlers = new ArrayList<ClientHandler>();
		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new Server().start();
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				Socket socket = serverSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ClientHandler handler = new ClientHandler(in, out);
				handlers.add(handler);
				handler.addObserver(this);
				new Thread(handler).start();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * Receives an updated room from one client and sends
	 * it to all the rest.
	 */
	public void update(Observable arg0, Object arg1)
	{
		Room room = (Room) arg1;
		for (ClientHandler handler : handlers)
		{
			handler.updateClient(room);
		}
	}
}
