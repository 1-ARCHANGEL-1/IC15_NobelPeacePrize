package edu.miracostacollege.cs112.ic15_nobelpeaceprize.controller;

import edu.miracostacollege.cs112.ic15_nobelpeaceprize.model.Model;
import edu.miracostacollege.cs112.ic15_nobelpeaceprize.model.NobelLaureate;
import javafx.collections.ObservableList;

/**
 * The <code>Controller</code> is a Singleton object that relays all commands between the Model and View
 * (and vice versa).  There is only one Controller object, accessible by a call to the static getInstance()
 * method.
 *
 * @author Michael Paulding
 * @version 1.0
 */
public class Controller {

	private static Controller theInstance;
	private ObservableList<NobelLaureate> mAllLaureatesList;

	//no one outside the class can use this constructor
	private Controller()
	{}
	/**
	 * Gets the one instance of the Controller.
	 * @return The instance
	 */
	public static Controller getInstance()
	{

		//If the instance is null, create a new object
		if(theInstance == null){
			theInstance = new Controller();
			//fill the allLaureatelist with data from Model
			//if the binary file has data, fill with Binary file
			if(Model.binaryFileHasData())
				theInstance.mAllLaureatesList = Model.populateListFromBinaryFile();
				//otherwise fill with csv file
			else
				theInstance.mAllLaureatesList = Model.populateListFromCSVFile();

		}
		//Otherwise, return theInstance
		return theInstance;
	}

	/**
	 * Gets the list of all laureates.
	 * @return The list of all laureates.
	 */
	public ObservableList<NobelLaureate> getAllLaureates() {
		return mAllLaureatesList;
	}

	/**
	 * Makes a request for the model to save all the laureates data (the list of all laureates) to
	 * a persistent binary file.
	 */
	public void saveData() {
		Model.writeDataToBinaryFile(mAllLaureatesList);
	}
}
