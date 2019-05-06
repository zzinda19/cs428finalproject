package ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Action;
import javax.swing.JButton;

public class Button extends JButton
{
	private static final long serialVersionUID = 1L;
	
	private static final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);

	public Button(String text, Dimension size)
	{
		super(text);
		setSize(size);
		setFont(font);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	public Button(Action action, Dimension size)
	{
		super(action);
		setSize(size);
		setFont(font);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	public void setSize(Dimension size)
	{
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}
}
