package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Action;

public class SubmitButton extends Button
{
	private static final long serialVersionUID = 1L;
	
	private static final Color backgroundColor = new Color(0, 116, 217);
	private static final Color fontColor = Color.WHITE;
	private static final Dimension size = new Dimension(300, 40);

	public SubmitButton(String text)
	{
		super(text, size);
		setBackground(backgroundColor);
		setForeground(fontColor);
	}
	
	public SubmitButton(Action action)
	{
		super(action, size);
		setBackground(backgroundColor);
		setForeground(fontColor);
	}
}
