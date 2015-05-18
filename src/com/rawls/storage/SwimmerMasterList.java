package com.rawls.storage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import com.rawls.data.Swimmer;
import com.rawls.main.RecorderMain;

public class SwimmerMasterList {
	
	static ArrayList<Swimmer> swimmerList;

	
	public static void initList(Swimmer[] s)
	{
		swimmerList = new ArrayList<Swimmer>();
		
		for(int i = 0; i < s.length; i++)
		{
			swimmerList.add(s[i]);
		}
	}
	
	public static void initList()
	{
		swimmerList = new ArrayList<Swimmer>();
	}
	
	public static Swimmer getSwimmer(int index)
	{
		return swimmerList.get(index);
	}
	
	private static void sortAndAssignIDs()
	{
		Swimmer[] temp = new Swimmer[swimmerList.size()];
		
		for(int i = 0; i < swimmerList.size(); i++)
		{
			temp[i] = swimmerList.get(i);
		}
		
		Arrays.sort(temp);
		
		for(int i = 0; i < temp.length; i++)
		{
			swimmerList.set(i, temp[i]);
			swimmerList.get(i).assignID(i);
		}
		
		
		
	}
	
	public static void addSwimmer(Swimmer s)
	{	
		swimmerList.add(s);
		sortAndAssignIDs();
		
		RecorderMain.updateStatus(s.getFormattedName() + " added!");
	}
	
	public static void removeSwimmer(int index)
	{
		swimmerList.remove(index);
		sortAndAssignIDs();
		RecorderMain.updateStatus("Swimmer Removed");
	}
	
	public static Vector<String> getListDisplay()
	{
		Vector<String> listData = new Vector<String>();
		
		if(swimmerList.size() == 0)
		{
			listData.add("No Swimmers");
			return listData;
		}
		else
		{
			for(int i = 0; i < swimmerList.size(); i++)
			{
				listData.add(swimmerList.get(i).getDisplayName());
			}
			
			return listData;
		}
	}
	
	public static ArrayList<Swimmer> getList()
	{
		return swimmerList;
	}
	
	public static ArrayList<Swimmer> getFastestData()
	{
		ArrayList<Swimmer> fast = new ArrayList<Swimmer>();
		
		for(int i = 0; i < swimmerList.size(); i++)
		{
			fast.add(swimmerList.get(i).getFastestData());
		}
		
		return fast;
	}
	
	public static void sort()
	{
		sortAndAssignIDs();
	}
	
	public static boolean hasSwimmers()
	{
		if(swimmerList.size() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static String[] getFormattedNames()
	{
		if(!hasSwimmers())
		{
			String[] out = {"No Swimmers"};
			return out;
		}
		else
		{
			String[] out1 = new String[swimmerList.size()];
			
			for(int i = 0; i < swimmerList.size(); i++)
			{
				out1[i] = swimmerList.get(i).getFormattedName();
			}
			
			return out1;
		}
	}
	
}
