package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import controller.RoomViewController;
import model.Room;
import ui.Button;
import ui.ErrorLabel;
import ui.HeaderLabel;
import ui.RoomLabel;
import ui.SubmitButton;

public class RoomView implements View
{
	private static final Dimension cardSize = new Dimension(300, 200);
	
	private Room model;
	private RoomViewController viewController;
	
	private InnerRoomView innerView;
	private JPanel panel;
	private HeaderLabel headerLabel;
	private JPanel subheader;
	private RoomLabel nameLabel;
	private RoomLabel divider;
	private RoomLabel codeLabel;
	
	private Button uploadButton;
	private ErrorLabel errorLabel;
	
	private List<InnerFileView> fileViews;
	
	public RoomView(Room model, RoomViewController viewController)
	{
		this.model = model;
		this.viewController = viewController;
		initializeComponents();
	}
	
	private void initializeComponents()
	{
		innerView = new InnerRoomView();
		initializeInnerView();
		panel = innerView.getPanel();
	}
	
	private void initializeInnerView()
	{
		initializeHeader();
		initializeSubheader();
		initializeCenterPanel();
		initializeErrorLabel();
		innerView.addToHeader(headerLabel);
		innerView.addToTop(subheader);
		innerView.addToBottom(errorLabel);
	}
	
	private void initializeHeader()
	{
		headerLabel = new HeaderLabel("SpeechDrop");
		headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		headerLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				viewController.actionPerformed(new ActionEvent(headerLabel, 0, "Back"));
			}
		});
	}
	
	private void initializeSubheader()
	{
		subheader = new JPanel();
		nameLabel = new RoomLabel(model.getName(), Color.BLACK, true);
		divider = new RoomLabel("|", Color.DARK_GRAY, true);
		codeLabel = new RoomLabel(model.getCode(), Color.DARK_GRAY, true);
		subheader.add(nameLabel);
		subheader.add(divider);
		subheader.add(codeLabel);
	}
	
	private void initializeCenterPanel()
	{
		initializeFileViews();
		initializeUploadButton();
		for (InnerFileView innerFileView : fileViews)
		{
			innerView.addToCenter(innerFileView.getPanel());
		}
		innerView.addToCenter(uploadButton);
	}
	
	private void initializeUploadButton()
	{
		uploadButton = new SubmitButton("Click to upload");
		uploadButton.setSize(cardSize);
		uploadButton.setActionCommand("Upload");
		uploadButton.addActionListener(viewController);
	}
	
	private void initializeFileViews()
	{
		fileViews = new LinkedList<InnerFileView>();
		InnerFileView fileView;
		for (String file : model.getFiles())
		{
			fileView = new InnerFileView(viewController, file, cardSize);
			fileViews.add(fileView);
		}
	}
	
	public void updateView(Room room)
	{
		this.model = room;
		innerView.clearCenterPanel();
		initializeCenterPanel();
		innerView.repaintCenterPanel();
	}
	
	public void addFileView(String file)
	{
		InnerFileView fileView = new InnerFileView(viewController, file, cardSize);
		innerView.addToCenter(fileView.getPanel());
	}
	
	private void initializeErrorLabel()
	{
		errorLabel = new ErrorLabel("");
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public void displayText(String text)
	{
		errorLabel.setText(text);
	}

	public void displayError(String message)
	{
		errorLabel.displayError(message);
	}
	
	public void clear() {};
}
