package com.rawls.io;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;

import com.rawls.data.Record;
import com.rawls.data.Swimmer;
import com.rawls.main.RecorderMain;
import com.rawls.storage.SwimmerMasterList;
import com.thoughtworks.xstream.XStream;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.fonts.apps.PFMReader;

public class DataExporter {
	
	public static final String NEWLINE = System.lineSeparator();
	
	private static XStream xstream = new XStream();
	
	public static void setMapping()
	{
		xstream.alias("swimmer", Swimmer.class);
		xstream.alias("record", Record.class);
		
		RecorderMain.updateStatus("XML mapping complete, ready to save");
	}
	
	//For General use (not bookmarks) uses an objectstream to write out one ArrayList full of swimmers (save a step or two when reloading later) 
	public static void exportToXML(File aFile)
	{
		
		ArrayList<Swimmer> swimmers = SwimmerMasterList.getList();
		
		//Before going to XML we have to make sure all swimmers have at least one record. If they don't the xml won't work right.
		for(int i = 0; i < swimmers.size(); i++)
		{
			if(!swimmers.get(i).hasRecords())
			{
				swimmers.get(i).addRecord(new Record());
			}
		}
		
		try {
			
			ObjectOutputStream oop = xstream.createObjectOutputStream(new FileWriter(aFile));
			
			oop.writeObject(swimmers);
			
			oop.close();
			
			RecorderMain.updateStatus("Database XML File Written");
			
		} catch (IOException e) {
			RecorderMain.updateStatus("File Write Error, data not written");
		}
		
		//Use the XML Object Stream Parser (Used for creating a string)
		/*
		String output = xstream.toXML(swimmers);
		
		RecorderMain.updateStatus("Printing XML file for perusal\n");
		
		RecorderMain.updateStatus(output);
		*/	
	}
	
	public static void exportBookMarkXML(File aFile)
	{
		ArrayList<Swimmer> swimmers = SwimmerMasterList.getList();
		
		//Ensure than no swimmers have no record entries
		for(int i = 0; i < swimmers.size(); i++)
		{
			if(!swimmers.get(i).hasRecords())
			{
				swimmers.remove(i);
			}
		}
		
		String output = xstream.toXML(swimmers);

		RecorderMain.updateStatus("Converting data to XML");
		
		try 
		{
			PrintWriter pw = new PrintWriter(aFile);
			
			pw.print(output);
			
			pw.close();
			RecorderMain.updateStatus("Bookmark XML File Written");
		}
		catch (FileNotFoundException e) 
		{
			RecorderMain.updateStatus("File Write Error");
		}
	}
	
	public static void renderPDF(File pdfFile, File xslFile, File xmlFile)
	{
		try {
		FopFactory fopFactory = FopFactory.newInstance();
		OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
			try {
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(new StreamSource(xslFile));
				
				transformer.setParameter("versionParam", "2.0");
				
				Source src = new StreamSource(xmlFile);
				Result res = new SAXResult(fop.getDefaultHandler());
				
				transformer.transform(src, res);
				
			} finally{
				out.close();
			}
			RecorderMain.updateStatus("Success!");
		} catch (Exception e) {
			RecorderMain.updateStatus("PDF not created.");
		}
	}
	
/* Legacy Methods
	public static void saveData(File aFile)
	{
		ArrayList<Swimmer> swimmers = SwimmerMasterList.getList();
		
		//Before going to XML we have to make sure all swimmers have at least one record. If they don't the xml won't work right.
		for(int i = 0; i < swimmers.size(); i++)
		{
			if(!swimmers.get(i).hasRecords())
			{
				swimmers.get(i).addRecord(new Record());
			}
		}
		
		try {
			
			ObjectOutputStream oop = xstream.createObjectOutputStream(new FileWriter(aFile));
			
			oop.writeObject(swimmers);
			
			oop.close();
			
			RecorderMain.updateStatus("Database XML File Written");
			
		} catch (IOException e) {
			RecorderMain.updateStatus("File Write Error, data not written");
		}
	}
	
	public static boolean saveDataAS(File aFile)
	{
		ArrayList<Swimmer> swimmers = SwimmerMasterList.getList();
		
		//Before going to XML we have to make sure all swimmers have at least one record. If they don't the xml won't work right.
		for(int i = 0; i < swimmers.size(); i++)
		{
			if(!swimmers.get(i).hasRecords())
			{
				swimmers.get(i).addRecord(new Record());
			}
		}
		
		try {
			
			ObjectOutputStream oop = xstream.createObjectOutputStream(new FileWriter(aFile));
			
			oop.writeObject(swimmers);
			
			oop.close();
			
			RecorderMain.updateStatus("Database XML File Written");
			
			return true;
			
		} catch (IOException e) {
			RecorderMain.updateStatus("File Write Error, data not written");
			return false;
		}
	}
	
	*/
	
	public static void exportTimeSheet(File aFile)
	{
		
		//Prepare the String for the file first
		ArrayList<Swimmer> temp = SwimmerMasterList.getList();
		
		String out = "";
		
		for(int i = 0; i < temp.size(); i++)
		{
			out += temp.get(i).getFormattedName() + ":" + NEWLINE;
			
			if(temp.get(i).hasRecords())
			{
				for(int j = 0; j < temp.get(i).getRecordListPrint().size(); j++)
				{
					out += "\t" + temp.get(i).getRecordListPrint().get(j) + NEWLINE;
				}
			}
			
			out += "\n";
		}
		
		try
		{
			PrintWriter pOut = new PrintWriter(aFile);
			
			pOut.print(out);
			
			pOut.close();
			
			RecorderMain.updateStatus("Time Sheet Export Complete!\n");
		}
		catch(Exception e)
		{
			RecorderMain.updateStatus("Time export error, data not written to file!");
		}
	}
	
	public static void exportSwimmerDetails(File aFile, Swimmer s)
	{
		//Prepare the String to be written
		Vector<String> records = s.getRecordList();
		
		String out = s.getFormattedName();
		
		for(int i =  0; i < records.size(); i++)
		{
			out += "\t" + records.get(i) + NEWLINE;
		}
		
		try
		{
			PrintWriter pOut = new PrintWriter(aFile);
			
			pOut.print(out);
			
			pOut.close();
			
			RecorderMain.updateStatus("Swimmer Data Exported Successfully!\n");
		}
		catch(Exception e)
		{
			RecorderMain.updateStatus("Export Error! Data not written to file!");
		}
	}
	
	/* Legacy Method
	public static void exportBookmarks(File aFile)
	{
		int currentLine = 1;
		int linesPerPage = 46;
		
		String out = "";
		
		ArrayList<Swimmer> temp = SwimmerMasterList.getFastestData();
		
		for(int i = 0; i < temp.size(); i++)
		{
			int linesNeeded = temp.get(i).getRecordList().size() + 1;
			
			if(linesPerPage - currentLine < linesNeeded)
			{
				for(currentLine = currentLine; linesPerPage - currentLine > 0; currentLine++)
				{
					out += NEWLINE;
				}
				
				currentLine = 1;
			}
			
			out += "\t\t\t" + temp.get(i).getFormattedName();
			currentLine++;
			
			if(temp.get(i).hasRecords())
			{
				for(int j = 0; j < temp.get(i).getRecordList().size(); j++)
				{
					out += "\t" + temp.get(i).getRecord(j) + NEWLINE;
					currentLine++;
				}
			}
			
			out += NEWLINE + NEWLINE + NEWLINE;
			currentLine += 3;
			
		}
		
		try
		{
			PrintWriter pOut = new PrintWriter(aFile);
			pOut.print(out);
			pOut.close();
			RecorderMain.updateStatus("Bookmarks exported!\n");
		}
		catch(Exception e)
		{
			RecorderMain.updateStatus("Bookmark export error, not written to file!");
		}
		
	}
*/
}