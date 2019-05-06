package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

public class CenteredJPanel extends CustomJPanel
{
	private static final long serialVersionUID = 1L;
	
	public CenteredJPanel()
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void addCenteredComponent(JComponent component)
	{
		super.add(Box.createVerticalGlue());
		super.add(component);
		component.setAlignmentX(CENTER_ALIGNMENT);
		super.add(Box.createVerticalGlue());
	}
	
	public void addPaddedComponent(JComponent component, int padding)
	{
		super.add(Box.createVerticalStrut(padding));
		super.add(component);
		component.setAlignmentX(CENTER_ALIGNMENT);
		super.add(Box.createVerticalStrut(padding));
	}

}
