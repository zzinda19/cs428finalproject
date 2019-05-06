package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Room implements Serializable
{
	private static final long serialVersionUID = 2880664318048634787L;
	
	private final String code;
	private final String name;
	private List<String> fileNames;
	private Date lastChanged;
	private Calendar calendar;
	
	public Room(String code, String name)
	{
		this.code = code;
		this.name = name;
		fileNames = new LinkedList<String>();
		calendar = Calendar.getInstance();
		lastChanged = calendar.getTime();
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
		lastChanged = calendar.getTime();
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
	
	public boolean isExpired()
	{
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime lastChangedLocal = LocalDateTime.ofInstant(lastChanged.toInstant(), zoneId);
		LocalDate now = LocalDate.now();
		Period period = Period.between(lastChangedLocal.toLocalDate(), now);
		return period.getMonths() >= 1;
	}
}
