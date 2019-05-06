package ui;

import java.awt.Color;
import java.awt.Font;

public class HeaderLabel extends Label
{
	private static final long serialVersionUID = 1L;
	
	private static final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 30);
	
	public HeaderLabel(String text)
	{
		super(text);
		setForeground(Color.BLACK);
		setFont(font);
	}
}
