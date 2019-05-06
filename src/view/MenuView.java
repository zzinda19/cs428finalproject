package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.MenuViewController;
import ui.ErrorLabel;
import ui.HeaderLabel;
import ui.SubmitButton;
import ui.TextField;

public class MenuView implements View
{
	private MenuViewController viewController;
	
	private InnerCardView innerView;
	private JPanel panel;
	private HeaderLabel headerLabel;
	private TextField existingTextField;
	private ErrorLabel errorLabel;
	private TextField newTextField;
	private SubmitButton submitButton;
	
	private List<JComponent> components;
	
	public MenuView(MenuViewController viewController)
	{
		this.viewController = viewController;
		this.innerView = new InnerCardView();
		this.panel = this.innerView.getPanel();
		components = new ArrayList<JComponent>();
		initializeComponents();
		innerView.addComponents(components);
	}
	
	private void initializeComponents()
	{
		headerLabel = new HeaderLabel("SpeechDrop");
		existingTextField = new TextField("Type an existing room code", 6);
		existingTextField.addKeyListener(viewController);
		errorLabel = new ErrorLabel("6 characters left");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newTextField = new TextField("Type a new room name", 25);
		submitButton = new SubmitButton("Make Room");
		submitButton.addActionListener(viewController);
		components.add(headerLabel);
		components.add(existingTextField);
		components.add(errorLabel);
		components.add(newTextField);
		components.add(submitButton);
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public void clear()
	{
		existingTextField.reset();
		newTextField.reset();
		errorLabel.setText("6 characters left");
	}
	
	public String getExistingInput()
	{
		return existingTextField.getText();
	}
	
	public String getNewInput()
	{
		return newTextField.getText();
	}

	public void displayText(String text)
	{
		errorLabel.setText(text);
	}
	
	public void displayError(String message)
	{
		errorLabel.displayError(message);
	}
}
