package com.rawls.io;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import com.rawls.data.Record;
import com.rawls.data.Swimmer;
import com.rawls.main.RecorderMain;
import com.rawls.storage.SwimmerMasterList;
import com.thoughtworks.xstream.XStream;

public class DataImporter {
	
	private static XStream xstream = new XStream();
	
	public static void completeMapping()
	{
		xstream.alias("swimmer", Swimmer.class);
		xstream.alias("record", Record.class);
	}
	
	//Uses objectinputstream to handle export method
	public static void importFromXML(File xmlFile)
	{
		ObjectInputStream ois;
		try {
			ois = xstream.createObjectInputStream(new FileInputStream(xmlFile));
			
			ArrayList<Swimmer> temp;
			temp = (ArrayList<Swimmer>) ois.readObject();
			
			ois.close();
			
			for(int i = 0; i < temp.size(); i++)
			{
				
				//Clear any default records placed during export
				if(temp.get(i).getRecord(0).getEvent().equals("?"))
				{
					temp.get(i).removeRecord(0);
				}
				
				SwimmerMasterList.addSwimmer(temp.get(i));
		
			}
		}
		 catch (Exception e) {
			 
			RecorderMain.updateStatus("File Read Error, data not loaded");
		}
		
		
	}
	
	public static void importFromText(File textFile)
	{
		try {
			Scanner scan = new Scanner(textFile);
			
			while(scan.hasNext())
			{
				String in = scan.nextLine();
				String first = "";
				String last = "";
				
				for(int i = 0; i < in.length(); i++)
				{
					if(in.charAt(i) == ',')
					{
						last = in.substring(0, i);
						first = in.substring(i + 2);
					}
				}
				
				SwimmerMasterList.addSwimmer(new Swimmer(first, last));
				RecorderMain.updateStatus("Data loaded!\n");
				
			}
			
			scan.close();
			
		} catch (FileNotFoundException e) {
			RecorderMain.updateStatus("Could not open file, data not loaded!");
			RecorderMain.updateStatus("Data not loaded!\n");
			RecorderMain.printStartingOptions();
			RecorderMain.startingOptions();
		}
		
		RecorderMain.getDateandFile();
		
		
		
	}

	public static void importFromSerial(File serialFile)
	{
		try {
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serialFile));
			
			ArrayList<Swimmer> tempList = (ArrayList<Swimmer>) ois.readObject();
			
			for(int i = 0; i < tempList.size(); i++)
			{
				SwimmerMasterList.addSwimmer(tempList.get(i));
			}
			
			ois.close();
			
			RecorderMain.updateStatus("Data loaded!");
			RecorderMain.getDateandFile();
			
			
		} catch (FileNotFoundException e) {
			
			RecorderMain.updateStatus("File not found!\n");
			RecorderMain.printStartingOptions();
			
		} catch (IOException e) {
			
			RecorderMain.updateStatus("Could not load file!\n");
			RecorderMain.printStartingOptions();
			
		} catch (ClassNotFoundException e) {
			
			RecorderMain.updateStatus("Error reading data, file not loaded!\n");
			RecorderMain.printStartingOptions();
			
		}
	}
}
