package client;

public class Command
{
	private final CommandFlag flag;
	private String message;
	private Packet packet;
	
	public Command(CommandFlag flag)
	{
		this.flag = flag;
	}
	
	public CommandFlag getFlag()
	{
		return flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}
