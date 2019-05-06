package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.Room;

/**
 * @author zakzi
 * RoomDatabase stores and retrieves all files requested by the server.
 */
public class RoomDatabase
{
	private HashMap<String, Room> rooms;
	
	//codeFile.txt will store all room codes/names for persistent access.
	private static final String codeFileName = "codeFile.txt";
	private static final String delimiter = ", ";
	
	private static RoomDatabase database;
	
	private RoomDatabase()
	{
		rooms = new HashMap<String, Room>();
		getRoomsOnStartUp();
	}
	
	/**
	 *	RoomDatabase is a singleton, only one copy should ever
	 *	exist or be accessed at a time.
	 */
	public static RoomDatabase getInstance()
	{
		if (database == null)
		{
			database = new RoomDatabase();
		}
		return database;
	}
	
	/**
	 * 	Reads codeFile.txt upon startup and
	 *  re-creates all the corresponding rooms.
	 */
	private void getRoomsOnStartUp()
	{
		Path path = Paths.get(codeFileName);
		boolean pathExists = Files.exists(path);
		if (pathExists)
		{
			try
			{
				List<String> keyPairs = Files.readAllLines(path);
				String code, name;
				Room room;
				for (String keyPair : keyPairs)
				{
					String[] splitPair = keyPair.split(delimiter);
					code = splitPair[0];
					name = splitPair[1];
					System.out.println(code + " " + name);
					room = new Room(code, name);
					List<String> files = retrieveFilesFromDiskForRoom(code);
					room.setFiles(files);
					rooms.put(code, room);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Writes the room code and name to codeFile.txt to log
	 * them for persistent access.
	 */
	private void writeKeyPairToDisk(String code, String name)
	{
		String keyPair = new String(code + delimiter + name + "\r\n");
		Path path = Paths.get(codeFileName);
		boolean pathExists = Files.exists(path);
		if (pathExists)
		{
			try
			{
				Files.write(path, keyPair.getBytes(), StandardOpenOption.APPEND);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				
				Files.write(path, keyPair.getBytes(), StandardOpenOption.CREATE_NEW);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Builds a new and unique 6-digit room code,
	 * with only upper/lower characters and integers [0,9].
	 */
	public String getNewCode()
	{
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		boolean isChar, isUpper;
		char character;
		int digit;
		for (int index = 0; index < 6; index++)
		{
			isChar = r.nextBoolean();
			if (isChar)
			{
				character = (char) r.nextInt(26);
				isUpper= r.nextBoolean();
				if (isUpper)
				{
					character += 'A';
				}
				else
				{
					character += 'a';
				}
				sb.append(character);
			}
			else
			{
				digit = r.nextInt(10);
				sb.append(digit);
			}
		}
		String roomCode = sb.toString();
		// If this code already exists in the DB,
		// make a new one--code must be unique.
		if (containsCode(roomCode))
		{
			return getNewCode();
		}
		return roomCode;
	}
	
	/**
	 * Creates a new room with the specified name and
	 * adds it to the DB, then returns it.
	 */
	public Room newRoom(String name)
	{
		String code = getNewCode();
		Room room = new Room(code, name);
		addRoom(code, room);
		return room;
	}
	
	/**
	 * Returns the requested room object.
	 */
	public Room joinRoom(String code)
	{
		return getRoom(code);
	}
	
	/**
	 * Returns whether or not this code is valid,
	 * i.e., if it exists in the DB or not.
	 */
	public boolean isValidCode(String code)
	{
		return containsCode(code);
	}
	
	/**
	 * Writes a file to the specified room folder on the disk,
	 * then updates the room object in the DB.
	 */
	public void addFileToRoom(String code, String fileName, byte[] fileContents) throws IOException
	{
		writeFileToDiskForRoom(code, fileName, fileContents);
		Room room = getRoom(code);
		room.addFile(fileName);
		updateRoom(code, room);
	}
	
	/**
	 * Gets the specified file contents in bytes.
	 */
	public byte[] getFileForRoom(String code, String fileName)  throws Exception
	{
		return retrieveFileContentsFromDisk(code, fileName);
	}
	
	/**
	 * Removes the specified file from the disk, 
	 * then updates the room object in the DB.
	 */
	public Room deleteFileForRoom(String code, String fileName) throws Exception
	{
		removeFileFromDiskForRoom(code, fileName);
		Room room = getRoom(code);
		boolean success = room.removeFile(fileName);
		if (success)
		{
			updateRoom(code, room);
		}
		else
		{
			throw new FileNotFoundException();
		}
		return room;
	}
	
	/**
	 * Writes a file to the disk in the specified room folder.
	 */
	private Path writeFileToDiskForRoom(String code, String fileName, byte[] fileContents) throws IOException
	{
		Path directory = Paths.get("db", code);
		boolean directoryExists = Files.exists(directory);
		if (directoryExists == false)
		{
			Files.createDirectories(directory);
		}
		Path path = Paths.get(directory.toString(), fileName);
		Files.write(path, fileContents, StandardOpenOption.CREATE);
		return path;
	}
	
	/*
	 * Retrieves the specified file from the disk for the specified room folder.
	 */
	private byte[] retrieveFileContentsFromDisk(String code, String fileName) throws Exception
	{
		Path path = Paths.get("db", code, fileName);
		boolean pathExists = Files.exists(path);
		if (pathExists == false) throw new FileNotFoundException();
		byte[] fileContents;
		fileContents = Files.readAllBytes(path);
		return fileContents;
	}
	
	/*
	 * Removes the specified file from the disk for the specified room folder.
	 */
	private void removeFileFromDiskForRoom(String code, String fileName) throws Exception
	{
		Path path = Paths.get("db", code, fileName);
		boolean pathExists = Files.exists(path);
		if (pathExists == false) throw new FileNotFoundException();
		Files.delete(path);
		// If the directory is empty delete the room folder.
		boolean directoryEmpty = isDirectoryForRoomEmpty(code);
		if (directoryEmpty)
		{
			path = Paths.get("db", code);
			Files.delete(path);
		}
	}
	
	/**
	 * Checks if the specified room folder is empty.
	 */
	private boolean isDirectoryForRoomEmpty(String code)
	{
		List<String> files = retrieveFilesFromDiskForRoom(code);
		return files.size() == 0;
	}
	
	/*
	 * Retrieves the list of file names in the specified room folder
	 * and sorts them by last modified.
	 */
	private List<String> retrieveFilesFromDiskForRoom(String code)
	{
		List<File> files = new LinkedList<File>();
		Path dir = Paths.get("db", code);
		boolean exists = Files.exists(dir);
		if (exists)
		{
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir))
			{
			    for (Path file: stream)
			    {
			        files.add(file.toFile());
			    }
			}
			catch (IOException | DirectoryIteratorException e) 
			{
			    e.printStackTrace();
			}
		}
		files = sortFilesByLastModified(files);
		List<String> fileNames = getFileNamesFrom(files);
		return fileNames;
	}
	
	/*
	 * Sorts a list of files by last modified.
	 */
	private List<File> sortFilesByLastModified(List<File> files)
	{
		Collections.sort(files, Comparator.comparingLong(File::lastModified));
		return files;
	}
	
	/*
	 * Converts a list of File objects to the list of their names.
	 */
	private List<String> getFileNamesFrom(List<File> files)
	{
		List<String> fileNames = new LinkedList<String>();
		for (File file : files)
		{
			fileNames.add(file.getName());
		}
		return fileNames;
	}
	
	/*
	 * Adds a room to the database using a synchronized, thread-safe
	 * map.
	 */
	private void addRoom(String code, Room room)
	{
		Map<String, Room> map = Collections.synchronizedMap(rooms);
		synchronized(map)
		{
			map.put(code, room);
			rooms = new HashMap<String, Room>(map);
			writeKeyPairToDisk(code, room.getName());
		}
	}
	
	/*
	 * Gets a room from the database using a synchronized, thread-safe
	 * map.
	 */
	public Room getRoom(String code)
	{
		Map<String, Room> map = Collections.synchronizedMap(rooms);
		synchronized(map)
		{
			return map.get(code);
		}
	}
	
	/*
	 * Checks if a room code is contained in the database using a synchronized, thread-safe
	 * map.
	 */
	private boolean containsCode(String code)
	{
		Map<String, Room> map = Collections.synchronizedMap(rooms);
		synchronized(map)
		{
			return map.containsKey(code);
		}
	}
	
	/*
	 * Updates a room in the database using a synchronized, thread-safe
	 * map.
	 */
	private void updateRoom(String code, Room room)
	{
		Map<String, Room> map = Collections.synchronizedMap(rooms);
		synchronized(map)
		{
			map.replace(code, room);
			rooms = new HashMap<String, Room>(map);
		}
	}

}
