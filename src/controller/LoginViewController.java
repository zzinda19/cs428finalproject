package controller;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import client.Command;
import client.CommandFlag;
import client.Packet;
import client.PacketFlag;
import view.LoginView;

public class LoginViewController extends Observable implements ViewController
{
	public static final String name = "LOGIN";
	private LoginView view;
	
	public LoginViewController()
	{
		view = new LoginView(this);
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
			default:
				break;
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		if (action.equals("Connect")) {
			String ip = view.getInput();
			boolean validIp = isValidIp(ip);
			if (!validIp)
			{
				displayError("Improperly formatted IP");
			}
			displayText("Connecting...");
			Command command = new Command(CommandFlag.OPEN_SOCKET);
			command.setMessage(ip);
			transferToClient(command);
		}
	}
	
	public void clear()
	{
		view.clear();
	}

	private boolean isValidIp(String ip)
	{
		Pattern pattern = Pattern.compile("((\\d{1,3})(.)){3}(\\d{1,3})");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	
	private void transferToClient(Command command)
	{
		setChanged();
		notifyObservers(command);
	}
}
