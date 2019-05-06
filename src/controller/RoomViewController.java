package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import javax.swing.JPanel;

import client.Command;
import client.CommandFlag;
import client.Packet;
import client.PacketFlag;
import model.Room;
import view.RoomView;

public class RoomViewController extends Observable implements ViewController
{
	public final String name;
	private Room model;
	private RoomView view;
	
	public RoomViewController(Room model)
	{
		this.model = model;
		this.name = "ROOM " + model.getCode();
		this.view = new RoomView(this.model, this);
	}
	
	public JPanel getView()
	{
		return view.getPanel();
	}

	public void displayText(String text)
	{
		view.displayText(text);
	}

	public void displayError(String message)
	{
		view.displayError(message);
	}

	public void receive(Packet packet)
	{
		PacketFlag flag = packet.getFlag();
		switch(flag)
		{
			case ERROR:
				displayError(packet.getMessage());
				break;
			case FILE:
				downloadFileFromPacket(packet);
				break;
			case ROOM:
				updateView(packet.getRoom());
				break;
			default:
				break;
		}
	}
	
	public void clear()
	{
		view.clear();
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////
//	ACTION LISTENERS																			  //
////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		Command command;
		if (action.equals("Back"))
		{
			command = new Command(CommandFlag.BACK_TO_MENU);
			command.setMessage(MenuViewController.name);
			transferToClient(command);
		}
		else if (action.equals("Upload"))
		{
			command = new Command(CommandFlag.UPLOAD_FILE);
			command.setMessage(model.getCode());
			transferToClient(command);
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////
//	PRIMARIES																					  //
////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void sendDownloadOrDeleteRequestPacket(String fileName, PacketFlag flag)
	{
		Command command = new Command(CommandFlag.SEND_PACKET);
		Packet packet = new Packet(flag);
		packet.setMessage(model.getCode());
		packet.setFileName(fileName);
		command.setPacket(packet);
		transferToClient(command);
	}
	
	private void transferToClient(Command command)
	{
		setChanged();
		notifyObservers(command);
	}
	
	private void downloadFileFromPacket(Packet packet)
	{
		String fileName = packet.getFileName();
		Path path = Paths.get(System.getProperty("user.home"), "Downloads", fileName);
		byte[] fileContents = packet.getFileContents();
		try
		{
			Files.write(path, fileContents, StandardOpenOption.CREATE);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			displayError("An error occured downloading the file");
		}
	}
	
	private void updateView(Room room)
	{
		view.updateView(room);
	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////
//	HELPERS																						  //
////////////////////////////////////////////////////////////////////////////////////////////////////
}
