package ui;

import java.awt.Color;
import java.awt.Font;

public class ErrorLabel extends Label
{
	private static final long serialVersionUID = 1L;
	
	private static final Color errorColor = Color.RED;
	private static final Color fontColor = new Color(0, 116, 217);
	private static final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 18);

	public ErrorLabel(String text)
	{
		super(text);
		setForeground(fontColor);
		setFont(font);
	}

	public void setText(String text)
	{
		setForeground(fontColor);
		super.setText(text);
	}
	
	public void displayError(String message)
	{
		setForeground(errorColor);
		super.setText(message);
	}
}
