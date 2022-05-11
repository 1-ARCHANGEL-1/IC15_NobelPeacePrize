package edu.miracostacollege.cs112.ic15_nobelpeaceprize.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The <code>Model</code> class represents the business logic (data and calculations) of the application.
 * In the Nobel Peace Prize Laureates app, it either loads laureates from a CSV file (first load) or a binary file (all
 * subsequent loads).  It is also responsible for saving data to a binary file.
 *
 * @author Michael Paulding
 * @version 1.0
 */
public class Model {
	
	public static final String BINARY_FILE = "Laureates.dat";
	public static final String CSV_FILE = "NobelPeacePrizeWinners.csv";

	/**
	 * Determines whether the binary file exists and has data (size/length > 5L bytes).
	 * @return True if the binary file exists and has data, false otherwise.
	 */
	public static boolean binaryFileHasData()
	{
		File binaryFile = new File(BINARY_FILE);
		if(binaryFile.exists() && binaryFile.length() >=5)//empty file = 4 bytes
		 return true;
		else
			return false;
	}

	/**
	 * Populates the list of all laureates from the binary file. This will only be called once, the first time the app
	 * loaded to seed initial data from the CSV file.  All subsequent loads will be extracted from
	 * the binary file.be called everytime the application loads,
	 * @return The list of all laureates populated from the CSV file
	 */
	public static ObservableList<NobelLaureate> populateListFromCSVFile()
	{
		//New List, capacity
		ObservableList<NobelLaureate> allLaureates = FXCollections.observableArrayList();
		//Loop through entire CSV file
		String name;
		int awardYear;
		String motivation, country;
		double prizeAmount;
		String line;
		String[] parts;
		//Loop through entire csv file (ignore first row)
		try
		{
			Scanner fileScanner = new Scanner(new File(CSV_FILE));
			//Skip the headings
			fileScanner.nextLine();
			//Loop through the file
			while(fileScanner.hasNextLine())
			{
				//Read one line from CSV
				line = fileScanner.nextLine();
				parts = line.split(",");
				awardYear = Integer.parseInt(parts[0]);
				motivation = parts[9];
				name = parts[13];
				country = parts[26];
				prizeAmount = Double.parseDouble(parts[5]);
                //instantiate an object and add it to the list
				allLaureates.add(new NobelLaureate(name, awardYear, motivation,country, prizeAmount));
			}
			fileScanner.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error: "+ e.getMessage());
		}
		return allLaureates;
	}

	/**
	 * Populates the list of all laureates from the binary file. This will be called everytime the application loads,
	 * other than the very first time, since it needs initial data from CSV.
	 * @return The list of all laureates populated from the binary file
	 */
	public static ObservableList<NobelLaureate> populateListFromBinaryFile()
	{
		ObservableList<NobelLaureate> allLaureates = FXCollections.observableArrayList();
		try {


			ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(BINARY_FILE));
			//Read from Binary file into an array
			NobelLaureate[] array = (NobelLaureate[]) fileReader.readObject();
			//Loop through array and add each laureate to the list
			for(NobelLaureate nl: array)
				allLaureates.add(nl);
				fileReader.close();
		}catch(IOException | ClassNotFoundException e)
		{
			System.out.println("Error: "+e.getMessage());
		}
		return allLaureates;
	}

	/**
	 * Saves the list of all laureates to the binary file. This will be called each time the application stops,
	 * which occurs when the user exits/closes the app.  Note this method is called in the View, by the controller,
	 * during the stop() method.
	 * @return True if the data were saved to the binary file successfully, false otherwise.
	 */
	public static boolean writeDataToBinaryFile(ObservableList<NobelLaureate> allLaureatesList)
	{
		NobelLaureate[] array = new NobelLaureate[allLaureatesList.size()];
		//Lets copy all the list data into the array
		for (int i = 0; i < array.length; i++)
		{
			array[i] = allLaureatesList.get(i);
		}
		//Write to the binary file
		try {
			ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(BINARY_FILE));
			fileWriter.writeObject(array);
			fileWriter.close();
		}catch(IOException  e)
		{
			System.out.println("Error: "+e.getMessage());
			return false;
		}
		return  true;
	}

}
