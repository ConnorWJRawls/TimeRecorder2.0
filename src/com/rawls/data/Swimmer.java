package com.rawls.data;
import com.rawls.main.RecorderMain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Swimmer implements Serializable, Comparable<Swimmer>{

	private static final long serialVersionUID = 1L;
	private String lName, fName;
	private int swimmerIDNo, age;
	private ArrayList<Record> records;
	private String header = "";
	
	public Swimmer(String fName, String lName)
	{
		records = new ArrayList<Record>();
		this.fName = fName;
		this.lName = lName;
	}
	
	public String getFName()
	{
		return fName;
	}
	
	public String getLName()
	{
		return lName;
	}
	
	public String getDisplayName()
	{
		return lName + ", " + fName;
	}
	
	public String getFormattedName()
	{
		return fName + " " + lName;
	}
	
	public int getAge() 
	{
		return age;
	}
	
	public void setAge(int a) 
	{
		age = a;
		
		RecorderMain.updateStatus("Age changed to " + age);
	}
	
	public void setFName(String name)
	{
		fName = name;
		
		sortRecords();
		
		RecorderMain.updateStatus("First name changed to " + fName);
	}
	
	public void setLName(String name)
	{
		lName = name;
		
		sortRecords();
		
		RecorderMain.updateStatus("Last name changed to " + lName);
	}
	
	public void setHeader(String header)
	{
		this.header = header;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public boolean hasRecords()
	{
		if(records.size() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void addRecord(Record r)
	{
		records.add(r);
		
		sortRecords();
		
		RecorderMain.updateStatus("Record Added!");
	}
	
	public void removeRecord(int index)
	{
		records.remove(index);
		
		sortRecords();
		
		RecorderMain.updateStatus("Record Removed!");
	}
	
	public void removeRecord(Record r)
	{
		records.remove(r);
		
		sortRecords();
		
		RecorderMain.updateStatus("Record Removed!");
	}
	
	public Record getRecord(int index)
	{
		return records.get(index);
	}
	
	public Record getRecord(Record r)
	{
		for(int i = 0; i < records.size(); i++)
		{
			if(records.get(i).equals(r))
			{
				return records.get(i);
			}
		}
		
		return new Record();
	}
	
	public void assignID(int IDNo)
	{
		swimmerIDNo = IDNo;
	}
	
	public int getSwimmerID()
	{
		return swimmerIDNo;
	}
	
	public Swimmer getFastestData()
	{
		Swimmer s = new Swimmer(fName, lName);
		
		if(records.size() > 0)
		{
			Record[] temp = new Record[records.size()];
		
			for(int i = 0; i < records.size(); i++)
			{
				temp[i] = records.get(i);
			}
		
			Arrays.sort(temp);
		
			String currentEvent = "";
			
			for(int i = 0; i < temp.length; i++)
			{
				if(i == 0)
				{
					currentEvent = temp[i].getEvent();
					s.addRecord(temp[i]);
				}
				else if(!temp[i].getEvent().equals(currentEvent))
				{
					currentEvent = temp[i].getEvent();
					s.addRecord(temp[i]);
				}
			}
		}
		
		return s;
		
	}
	
	public void sortRecords()
	{
		Record[] temp = new Record[records.size()];
		
		for(int i = 0; i < records.size(); i++)
		{
			temp[i] = records.get(i);
		}
		
		Arrays.sort(temp);
		
		for(int i = 0; i < temp.length; i++)
		{
			records.set(i, temp[i]);
		}
	}
	
	public Vector<String> getRecordList()
	{
		Vector<String> out = new Vector<String>();
		
		if(records.size() > 0)
		{
			for(int i = 0; i < records.size(); i++)
			{
				out.add(records.get(i).toString());
			}
		}
		else
		{
			out.add("No Records");
		}
		
		return out;
	}
	
	public void updateLegacyData()
	{
		if(hasRecords())
		{
			for(int i = 0; i < records.size(); i++)
			{
				records.get(i).generateFormattedTimeandDate();
			}
		}
	}
	
	public Vector<String> getRecordListPrint()
	{
		Vector<String> out = new Vector<String>();
		
		if(records.size() > 0)
		{
			for(int i = 0; i < records.size(); i++)
			{
				out.add(records.get(i).toStringExport());
			}
		}
		else
		{
			out.add("No Records");
		}
		
		return out;
	}
	
	@Override
	public int compareTo(Swimmer o) {
		
		if (o.getAge() < age) {
			return 1;
		}
		else if (o.getAge() > age)
		{
			return -1;
		}
		else {
			if(o.getLName().compareToIgnoreCase(lName) < 0)
			{
				return 1;
			}
			else if(o.getLName().compareToIgnoreCase(lName) > 0)
			{
				return -1;
			}
			else
			{
				if(o.getFName().compareToIgnoreCase(fName) < 0)
				{
					return 1;
				}
				else if(o.getFName().compareToIgnoreCase(fName) > 0)
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
