package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.LoginViewController;
import ui.ErrorLabel;
import ui.HeaderLabel;
import ui.SubmitButton;
import ui.TextField;

public class LoginView implements View
{
	private LoginViewController viewController;
	
	private InnerCardView innerView;
	private JPanel panel;
	private HeaderLabel headerLabel;
	private TextField textField;
	private ErrorLabel errorLabel;
	private SubmitButton submitButton;
	
	private List<JComponent> components;
	
	public LoginView(LoginViewController viewController)
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
		textField = new TextField("Type an IP address", 15);
		errorLabel = new ErrorLabel("Enter an IP address");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submitButton = new SubmitButton("Connect");
		submitButton.addActionListener(viewController);
		components.add(headerLabel);
		components.add(textField);
		components.add(errorLabel);
		components.add(submitButton);
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public void clear()
	{
		textField.reset();
		errorLabel.setText("Enter an IP address");
	}
	
	public String getInput()
	{
		return textField.getText();
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
