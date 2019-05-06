package ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextField;

public class Label extends JTextField
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension size = new Dimension(300, 40);

	public Label(String text)
	{
		super(text);
		setSize(size);
		setBackground(Color.WHITE);
		setEditable(false);
		setBorder(null);
	}
	
	public void setSize(Dimension size)
	{
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
}
