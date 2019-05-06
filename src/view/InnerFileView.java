package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.RoomViewController;
import ui.CustomJPanel;
import ui.FileNameLabel;
import ui.SubmitButton;
import utility.FileAction;

public class InnerFileView implements View
{
	private Dimension outerPanelSize;
	private Dimension buttonPanelSize;
	private Dimension buttonSize;
	
	private CustomJPanel outerPanel;
	private CustomJPanel buttonPanel;
	
	private JTextArea fileNameLabel;
	private SubmitButton downloadButton;
	private SubmitButton deleteButton;
	
	private String fileName;
	private RoomViewController viewController;

	public InnerFileView(RoomViewController viewController, String fileName, Dimension size)
	{	
		this.viewController = viewController;
		this.fileName = fileName;
		setSizes(size);
		initializeComponents();
	}
	
	private void setSizes(Dimension size)
	{
		outerPanelSize = size;
		buttonPanelSize = new Dimension(size.width, 60);
		buttonSize = new Dimension(buttonPanelSize.width / 2, 60);
	}
	
	private void initializeComponents()
	{
		outerPanel = new CustomJPanel(new BorderLayout());
		outerPanel.setSize(outerPanelSize);
		initializeFileNameLabel();
		initializeButtonPanel();
		outerPanel.add(fileNameLabel, BorderLayout.CENTER);
		outerPanel.add(buttonPanel, BorderLayout.PAGE_END);
	}
	
	private void initializeFileNameLabel()
	{
		fileNameLabel = new FileNameLabel(fileName);
	}
	
	private void initializeButtonPanel()
	{
		buttonPanel = new CustomJPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		buttonPanel.setSize(buttonPanelSize);
		buttonPanel.setBackground(Color.WHITE);
		initializeDownloadButton();
		initializeDeleteButton();
		buttonPanel.add(downloadButton);
		buttonPanel.add(deleteButton);
	}
	
	private void initializeDownloadButton()
	{
		Action downloadAction = new FileAction("Download", fileName, viewController);
		downloadButton = new SubmitButton(downloadAction);
		downloadButton.setSize(buttonSize);
	}
	
	private void initializeDeleteButton()
	{
		Action deleteAction = new FileAction("Delete", fileName, viewController);
		deleteButton = new SubmitButton(deleteAction);
		deleteButton.setSize(buttonSize);
		deleteButton.setBackground(Color.RED);
	}
	
	public JPanel getPanel()
	{
		return outerPanel;
	}
	
	public void clear() {};
}
