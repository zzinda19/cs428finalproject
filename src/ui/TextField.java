package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.Document;

import utility.MaxCharacterLimit;

public class TextField extends JTextField implements FocusListener
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension size = new Dimension(300, 40);
	
	private static final Color hintColor = Color.GRAY;
	private static final Color fontColor = Color.BLACK;
	private static final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
	
	public final Document regular;
	public final Document maxCharacters;
	
	public final String hint;
	public boolean showingHint;

	public TextField(final String hint, int maxCharacters)
	{
		super(hint);
		this.hint = hint;
		this.maxCharacters = new MaxCharacterLimit(maxCharacters);
		setSize(size);
		setForeground(hintColor);
		setFont(font);
		setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		addFocusListener(this);
		regular = getDocument();
		showingHint = true;
	}
	
	public void setSize(Dimension size)
	{
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}
	
	public void reset()
	{
		super.setText("");
		showHint();
	}
	
	public void showHint()
	{
		setDocument(regular);
		setForeground(hintColor);
		super.setText(hint);
		showingHint = true;
	}
	
	public void clearHint()
	{
		setDocument(maxCharacters);
		super.setText("");
		setForeground(fontColor);
		showingHint = false;
	}
	
	@Override
	public String getText()
	{
		return showingHint ? "" : super.getText();
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		if (getText().isEmpty())
		{
			clearHint();
		}
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		if (getText().isEmpty())
		{
			showHint();
		}
	}
}
