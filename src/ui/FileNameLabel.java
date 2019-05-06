package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class FileNameLabel extends JTextArea
{
	private static final long serialVersionUID = 1L;
	private static final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
	
	public FileNameLabel(String text)
	{
		super(text);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(5, 10, 10, 10)));
		setEditable(false);
		setFont(font);
		setLineWrap(true);
	}
}
