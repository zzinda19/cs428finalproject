package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import ui.CenteredJPanel;

public class InnerCardView implements View
{
	private CenteredJPanel outerPanel;
	private CenteredJPanel innerPanel;
	private CenteredJPanel contentPanel;
	
	public InnerCardView()
	{
		outerPanel = new CenteredJPanel();
		innerPanel = new CenteredJPanel();
		contentPanel = new CenteredJPanel();
		innerPanel.setSize(new Dimension(400, 400));
		innerPanel.setBackground(Color.WHITE);
		contentPanel.setBackground(Color.WHITE);
		innerPanel.addCenteredComponent(contentPanel);
		outerPanel.addCenteredComponent(innerPanel);
	}
	
	public void addComponents(List<JComponent> components)
	{
		for (JComponent component : components)
		{
			contentPanel.addPaddedComponent(component, 10);
		}
	}
	
	public JPanel getPanel()
	{
		return outerPanel;
	}
	
	public void clear() {};
}
