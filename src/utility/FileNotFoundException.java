package utility;

public class FileNotFoundException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private static final String message = "File not found";

	public FileNotFoundException()
	{
		super(message);
	}
	
	public String getMessage()
	{
		return message;
	}
}
