package utility;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import client.PacketFlag;
import controller.RoomViewController;

public class FileAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;

	private String fileName;
	private RoomViewController listener;
	
	public FileAction(String command, String fileName, RoomViewController listener)
	{
		super(command);
		this.fileName = fileName;
		this.listener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String command = arg0.getActionCommand();
		if (command.equals("Download"))
		{
			listener.sendDownloadOrDeleteRequestPacket(fileName, PacketFlag.DOWNLOAD);
		}
		else if (command.equals("Delete"))
		{
			listener.sendDownloadOrDeleteRequestPacket(fileName, PacketFlag.DELETE);
		}
	}
}
