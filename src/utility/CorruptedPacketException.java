package utility;

public class CorruptedPacketException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Packet corrupted: lost data or improperly flagged";

	public CorruptedPacketException()
	{
		super(message);
	}
	
	public String getMessage()
	{
		return message;
	}
}
