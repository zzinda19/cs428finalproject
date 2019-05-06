package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TabPanel extends CustomJPanel
{
	private static final long serialVersionUID = 1L;
	private static final int horizontalPadding = 30;
	private static final int verticalPadding = 15;

	public TabPanel()
	{
		super(new FlowLayout(FlowLayout.LEADING, horizontalPadding, verticalPadding));
		setAlignmentX(SwingConstants.LEADING);
	}
	
	public TabPanel(Dimension size)
	{
		super(new FlowLayout(FlowLayout.LEADING, horizontalPadding, verticalPadding));
		setAlignmentX(SwingConstants.LEADING);
		setSize(size);
	}
	
	public Component add(Component component)
	{
		if (component instanceof JTextField)
		{
			component.setBackground(this.getBackground());
		}
		return super.add(component);
	}
}
