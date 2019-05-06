package controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

import javax.swing.JPanel;

import client.Command;
import client.CommandFlag;
import client.Packet;
import client.PacketFlag;
import view.MenuView;

public class MenuViewController extends Observable implements ViewController, KeyListener
{
	public static final String name = "MENU";
	private MenuView view;
	
	private int numUsed;
	private int numLeft;
	
	public MenuViewController()
	{
		view = new MenuView(this);
		numUsed = 0;
		numLeft = 6;
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
		switch (flag)
		{
			case ERROR:
				displayError(packet.getMessage());
				break;
			case ROOM:
				transferToRoomViewController(packet);
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
		if (action.equals("Make Room"))
		{
			makeNewRoom();
		}
	}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e)
	{
		view.displayText(numLeft + " characters left");
		if (numLeft == 0)
		{
			joinRoom();
		}
	}
	
	public void keyTyped(KeyEvent e)
	{	
		numUsed = view.getExistingInput().length();
		numLeft = 6 - numUsed;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////
//	PRIMARIES																					  //
////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void makeNewRoom()
	{
		String name = view.getNewInput();
		Packet packet = new Packet(PacketFlag.NEW);
		packet.setMessage(name);
		Command command = encapsulatePacketInCommand(CommandFlag.SEND_PACKET, packet);
		transferToClient(command);
	}
	
	private void joinRoom()
	{
		String code = view.getExistingInput();
		Packet packet = new Packet(PacketFlag.JOIN);
		packet.setMessage(code);
		Command command = encapsulatePacketInCommand(CommandFlag.SEND_PACKET, packet);
		transferToClient(command);
	}
	
	private void transferToRoomViewController(Packet packet)
	{
		Command command = encapsulatePacketInCommand(CommandFlag.INIT_ROOM, packet);
		transferToClient(command);
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////
//	HELPERS																						  //
////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Command encapsulatePacketInCommand(CommandFlag flag, Packet packet)
	{
		Command command = new Command(flag);
		command.setPacket(packet);
		return command;
	}
	
	private void transferToClient(Command command)
	{
		setChanged();
		notifyObservers(command);
	}
}
