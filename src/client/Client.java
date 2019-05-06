package client;

import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.LoginViewController;
import controller.MenuViewController;
import controller.RoomViewController;
import controller.ViewController;
import model.Room;

public class Client implements Runnable, Observer
{
	private static final String title = "SpeechDrop";
	
	private static final int port = 6112;
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private JFrame frame;
	private JPanel panel;
	
	private ViewController currentController;
	private LoginViewController loginViewController;
	private MenuViewController menuViewController;
	private RoomViewController roomViewController;
	
	private final JFileChooser fileChooser;
	
	public Client()
	{
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		panel = new JPanel(new CardLayout());
		initializeViewControllers();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		fileChooser = new JFileChooser();
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() {
				new Client();
			}
		});	
	}
	
	@Override
	public void run()
	{
		Packet packet;
		while (true)
		{
			try
			{
				packet = (Packet) in.readUnshared();
				receive(packet);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				currentController.displayError(e.getMessage());
			}
			catch (IOException e)
			{
				e.printStackTrace();
				currentController.displayError(e.getMessage());
				break;
			}
		}
		close();
	}
	
	private void initializeViewControllers()
	{
		initializeLoginController();
		initializeMenuController();
	}
	
	private void initializeLoginController()
	{
		loginViewController = new LoginViewController();
		loginViewController.addObserver(this);
		panel.add(LoginViewController.name, loginViewController.getView());
		currentController = loginViewController;
	}
	
	private void initializeMenuController()
	{
		menuViewController = new MenuViewController();
		menuViewController.addObserver(this);
		panel.add(MenuViewController.name, menuViewController.getView());
	}
	
	public void open(String ip)
	{
		try
		{
			socket = new Socket(ip, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			new Thread(this).start();
			
			currentController.clear();
			showCard(MenuViewController.name);
			currentController = menuViewController;
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			currentController.displayError(e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			currentController.displayError(e.getMessage());
		}
	}
	
	public void send(Packet packet)
	{
		try
		{
			out.reset();
			out.writeUnshared(packet);
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			currentController.displayError(e.getMessage());
		}
	}
	
	public void receive(Packet packet)
	{
		currentController.receive(packet);
	}
	
	public void close()
	{
		try
		{
			socket.close();
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			currentController.displayError(e.getMessage());
		}
	}
	
	public void addCardToPanel(JPanel card)
	{
		panel.add(card);
	}
	
	public void showCard(String name)
	{
		currentController.clear();
		CardLayout layout = (CardLayout) panel.getLayout();
		layout.show(panel, name);
	}
	
	private void initializeRoom(Room room)
	{
		roomViewController = new RoomViewController(room);
		roomViewController.addObserver(this);
		panel.add(roomViewController.name, roomViewController.getView());
		showCard(roomViewController.name);
		currentController = roomViewController;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		Command command = (Command) arg1;
		CommandFlag flag = command.getFlag();
		Packet packet;
		try
		{
			switch (flag)
			{
				case BACK_TO_MENU:
					String name = command.getMessage();
					showCard(name);
					currentController = menuViewController;
					break;
				case INIT_ROOM:
					Room room = command.getPacket().getRoom();
					initializeRoom(room);
					break;
				case OPEN_SOCKET:
					String ip = command.getMessage();
					open(ip);
					break;
				case SEND_PACKET:
					packet = command.getPacket();
					send(packet);
					break;
				case UPLOAD_FILE:
					String code = command.getMessage();
					Path path = selectFile();
					if (path != null)
					{
						packet = encapsulateFileInPacket(code, path);
						send(packet);
					}
					break;
				default:
					break;
			}
		}
		catch(IOException e)
		{
			currentController.displayError("Could not read file");
		}
		
	}
	
	private Packet encapsulateFileInPacket(String code, Path path) throws IOException
	{
		byte[] fileContents = Files.readAllBytes(path);
		Packet packet = new Packet(PacketFlag.FILE);
		packet.setFileName(path.getFileName().toString());
		packet.setFileContents(fileContents);
		packet.setMessage(code);
		return packet;
	}
	
	private Path selectFile()
	{
		int returnValue = fileChooser.showOpenDialog(this.frame);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			String filePath = file.getPath();
			Path path = Paths.get(filePath);
			return path;
		}
		else return null;
	}
}
