package utility;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MaxCharacterLimit extends PlainDocument
{
	private static final long serialVersionUID = 1L;
	
	private int maxCharacters;

    public MaxCharacterLimit(int maxCharacters)
    {
    	super();
        this.maxCharacters = maxCharacters;
    }

    public void insertString(int offset, String string, AttributeSet a) throws BadLocationException 
    {
        if (getLength() + string.length() <= maxCharacters)
        {
        	super.insertString(offset, string, a);
        }
    }
    
    public void replace(int offset, int length, String string, AttributeSet a) throws BadLocationException
    {
        if ((getLength() + string.length() - length) <= maxCharacters)
        {
        	super.replace(offset, length, string, a);
        }
    }
}
