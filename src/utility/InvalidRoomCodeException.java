package utility;

public class InvalidRoomCodeException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Invalid room code";

	public InvalidRoomCodeException()
	{
		super(message);
	}
	
	public String getMessage()
	{
		return message;
	}
}
