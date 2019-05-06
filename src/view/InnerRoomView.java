package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ui.TabPanel;

public class InnerRoomView implements View
{
	private static final int tabHeight = 75;
	private static final Dimension tabSize = new Dimension(Integer.MAX_VALUE, tabHeight);
	
	private JPanel outerPanel;
	private JPanel headerPanel;
	private JPanel innerPanel;
	private JPanel topPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	public InnerRoomView()
	{
		initializeComponents();
	}
	
	/**
	 * 	Main component constructor.
	 *  Constructs the outer panel, which then constructs and contains the header and inner panels.
	 */
	private void initializeComponents()
	{
		outerPanel = new JPanel(new BorderLayout());
		initializeHeaderPanel();
		initializeInnerPanel();
		outerPanel.add(innerPanel, BorderLayout.CENTER);
		outerPanel.add(headerPanel, BorderLayout.PAGE_START);
	}
	
	/**
	 * Returns the outermost panel.
	 */
	public JPanel getPanel()
	{
		return outerPanel;
	}
	
	/**
	 *  Header constructor.
	 */
	private void initializeHeaderPanel()
	{
		headerPanel = new TabPanel(tabSize);
		headerPanel.setBackground(Color.WHITE);
	}
	
	/**
	 * Inner panel constructor. Constructs and contains three sub-panels--top, center, and bottom.
	 */
	private void initializeInnerPanel()
	{
		innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		innerPanel.add(Box.createVerticalStrut(15));
		initializeTopPanel();
		initializeCenterPanel();
		initializeBottomPanel();
		innerPanel.add(topPanel);
		innerPanel.add(centerPanel);
		innerPanel.add(bottomPanel);
	}
	
	/**
	 * Top panel constructor.
	 */
	private void initializeTopPanel()
	{
		topPanel = new TabPanel(tabSize);
	}
	
	/**
	 * Center panel constructor.
	 */
	private void initializeCenterPanel()
	{
		centerPanel = new TabPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));
		centerPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}
	
	public void clearCenterPanel()
	{
		centerPanel.removeAll();
	}
	
	public void repaintCenterPanel()
	{
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Bottom panel constructor.
	 */
	private void initializeBottomPanel()
	{
		bottomPanel = new TabPanel(tabSize);
	}
	
	/**
	 * Adds a component to the header panel.
	 * @param component - the component to add.
	 */
	public void addToHeader(JComponent component)
	{
		headerPanel.add(component);
	}
	
	/**
	 * Adds a component to the inner top panel.
	 * @param component - the component to add.
	 */
	public void addToTop(JComponent component)
	{
		topPanel.add(component);
	}
	
	/**
	 * Adds a component to the inner center panel.
	 * @param component - the component to add.
	 */
	public void addToCenter(JComponent component)
	{
		centerPanel.add(component);
	}
	
	/**
	 * Adds a component to the inner bottom panel.
	 * @param component - the component to add.
	 */
	public void addToBottom(JComponent component)
	{
		bottomPanel.add(component);
	}
	
	public void clear() {};
}
