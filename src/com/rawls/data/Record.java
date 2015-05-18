package com.rawls.data;
import java.io.Serializable;
import java.text.DecimalFormat;

public class Record implements Serializable, Comparable<Record> {

	private static final long serialVersionUID = 1L;

	private String event;
	
	private String formattedTime, formattedDate;
	
	private int mins, sec, mili, day, month, year;
	
	public Record(String event, int mins, int sec, int mili, int day, int month, int year)
	{
		this.event = event;
		this.mins = mins;
		this.sec = sec;
		this.mili = mili;
		this.day = day;
		this.month = month;
		this.year = year;
		
		formattedTime = getTime();
		formattedDate = getDate();
	}
	
	public Record()
	{
		event = "?";
		mins = -1;
		sec = -1;
		mili  = -1;
		day = -1;
		month = -1;
		year = -1111;
	}

	public String setEvent(String event)
	{
		this.event = event;
		
		return "Event updated";
	}
	
	public String setTime(int mins, int sec, int mili)
	{
		this.mins = mins;
		this.sec = sec;
		this.mili = mili;
		
		return "Time updated";
	}
	
	public String setDate(int day, int month, int year)
	{
		this.day = day;
		this.month = month;
		this.year = year;
		
		return "Date updated";
	}
	
	public String getEvent()
	{
		return event;
	}
	
	public String getTime()
	{
		DecimalFormat df = new DecimalFormat("00");
		return "" + df.format(mins) + ":" + df.format(sec) + "." + df.format(mili);
	}

	public String getDate()
	{
		return "" + month + "/" + day + "/" + year;
	}
	
	public String toString()
	{
		return event + "\t" + getTime() + " " + getDate();
	}
	
	public String toStringExport()
	{
		return event + "\t" + getTime() + " " + getDate();
	}

	public String exportString()
	{
		return event + "\t" + getTime() + "\t" + getDate();
	}
	
	public int getMins()
	{
		return mins;
	}
	
	public int getSec()
	{
		return sec;
	}
	
	public int getMili()
	{
		return mili;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public void generateFormattedTimeandDate()
	{
		createFormattedTime();
	}
	
	private void createFormattedTime()
	{
		formattedTime = getTime();
	}
	
	private void createFormattedDate()
	{
		formattedDate = getDate();
	}
	
	@Override
	public int compareTo(Record o) {
		
		//First Sort Events Alphabetically
		if(o.getEvent().compareToIgnoreCase(event) < 0)
		{
			return -1;
		}
		else if(o.getEvent().compareToIgnoreCase(event) > 0)
		{
			return 1;
		}
		else
		{
			if(o.getMins() < mins)
			{
				return 1;
			}
			else if(o.getMins() > mins)
			{
				return -1;
			}
			else
			{
				if(o.getSec() < sec)
				{
					return 1;
				}
				else if(o.getSec() > sec)
				{
					return -1;
				}
				else
				{
					if(o.getMili() < mili)
					{
						return 1;
					}
					else if(o.getMili() > mili)
					{
						return -1;
					}
					else
					{
						return 0;
					}
				}
			}
		}
		
	}

}
