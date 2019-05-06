package utility;

public class InvalidRoomNameException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Room name cannot be empty";

	public InvalidRoomNameException()
	{
		super(message);
	}
	
	public String getMessage()
	{
		return message;
	}
}
