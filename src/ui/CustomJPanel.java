package ui;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class CustomJPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public CustomJPanel()
	{
		super();
	}
	
	public CustomJPanel(LayoutManager layout)
	{
		super(layout);
	}

	public void setSize(Dimension size)
	{
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
}
