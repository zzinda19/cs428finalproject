package controller;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import client.Packet;

public interface ViewController extends ActionListener
{
	public JPanel getView();
	
	public void displayText(String text);
	
	public void displayError(String message);
	
	public void receive(Packet packet);
	
	public void clear();
}
