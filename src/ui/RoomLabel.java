package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

public class RoomLabel extends JTextField
{
	private static final long serialVersionUID = 1L;

	private static final Font headerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
	private static final Font subheaderFont = new Font(Font.SANS_SERIF, Font.BOLD, 24);
	
	public RoomLabel(String text, Color color, boolean isHeader)
	{
		super(text);
		setForeground(color);
		setFont(isHeader ? headerFont : subheaderFont);
		setEditable(false);
		setBorder(null);
	}
	
}
