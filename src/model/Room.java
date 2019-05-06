package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Room implements Serializable
{
	private static final long serialVersionUID = 2880664318048634787L;
	
	private final String code;
	private final String name;
	private List<String> fileNames;
	
	public Room(String code, String name)
	{
		this.code = code;
		this.name = name;
		fileNames = new LinkedList<String>();
	}
	
	public String getName()
	{
		return name;
	}

	public String getCode()
	{
		return code;
	}
	
	public void addFile(String file)
	{
		fileNames.add(file);
	}
	
	public boolean removeFile(String fileName)
	{
		return fileNames.remove(fileName);
	}
	

	public List<String> getFiles()
	{
		return fileNames;
	}
	
	public void setFiles(List<String> files)
	{
		this.fileNames = files;
	}
}
