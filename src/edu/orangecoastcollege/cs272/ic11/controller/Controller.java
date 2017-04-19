package edu.orangecoastcollege.cs272.ic11.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import edu.orangecoastcollege.cs272.ic11.model.Billionaire;
import edu.orangecoastcollege.cs272.ic11.model.DBModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {

	private static Controller theOne;

	private static final String DB_NAME = "cs272_test.db";
	private static final String TABLE_NAME = "billionaire";
	private static final String[] FIELD_NAMES = { "id", "name", "age", "gender", "worth", "citizenship", "sector", "political" };
	private static final String[] FIELD_TYPES = { "INTEGER PRIMARY KEY", "TEXT", "INTEGER", "TEXT", "REAL", "TEXT", "TEXT", "INTEGER" };
	private static final String DATA_FILE = "billionaires.csv";

	private ObservableList<Billionaire> mAllBillionairesList;
	private DBModel mDB;

	private Controller() {}

	public static Controller getInstance() {
		if (theOne == null) {
			theOne = new Controller();
			theOne.mAllBillionairesList = FXCollections.observableArrayList();

			try {
				theOne.mDB = new DBModel(DB_NAME, TABLE_NAME, FIELD_NAMES, FIELD_TYPES);
				theOne.initializeDBFromFile();
				
					ResultSet rs = theOne.mDB.getAllRecords();
				if (rs != null) {
					while (rs.next()) {
						int id = rs.getInt(FIELD_NAMES[0]);
						String name = rs.getString(FIELD_NAMES[1]);
						int age = rs.getInt(FIELD_NAMES[2]);
						String gender = rs.getString(FIELD_NAMES[3]);
						double worth = rs.getDouble(FIELD_NAMES[4]);
						String citizenship = rs.getString(FIELD_NAMES[5]);
						String sector = rs.getString(FIELD_NAMES[6]);
						boolean political = rs.getInt(FIELD_NAMES[7]) == 1;
						theOne.mAllBillionairesList
								.add(new Billionaire(id, name, age, gender, worth, citizenship, sector, political));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return theOne;
	}

	public ObservableList<Billionaire> getAllBillionares() {
		return theOne.mAllBillionairesList;
	}

	public ObservableList<String> getDistinctCitizenships() {
		ObservableList<String> citizenships = FXCollections.observableArrayList();
		citizenships.add(" ");
		for (Billionaire b : theOne.mAllBillionairesList)
			if (!citizenships.contains(b.getCitizenship()))
				citizenships.add(b.getCitizenship());
		FXCollections.sort(citizenships);
		return citizenships;
	}

	public ObservableList<String> getDistinctSectors() {
		ObservableList<String> sectors = FXCollections.observableArrayList();
		for (Billionaire b : theOne.mAllBillionairesList)
			if (!sectors.contains(b.getSector()))
				sectors.add(b.getSector());
		FXCollections.sort(sectors);
		return sectors;
	}

	public ObservableList<Billionaire> filter(String citizenship, String sector, double minWorth, double maxWorth) {
		ObservableList<Billionaire> filteredBillionairesList = FXCollections.observableArrayList();
		for (Billionaire b : theOne.mAllBillionairesList) {
			boolean toBeAdded = true;

			if ((citizenship != null && !b.getCitizenship().equals(citizenship))
					|| ((sector != null) && !b.getSector().equals(sector)) || b.getWorth() < minWorth
					|| b.getWorth() > maxWorth)
				toBeAdded = false;
			if (toBeAdded)
				filteredBillionairesList.add(b);
		}
		return filteredBillionairesList;
	}

	private int initializeDBFromFile() throws SQLException {
		int recordsCreated = 0;

		// If the result set contains results, database table already has
		// records, no need to populate from file (so return false)
		if (theOne.mDB.getRecordCount() > 0)
			return recordsCreated;

		try {
			// Otherwise, open the file (CSV file) and insert billionaire data
			// into database
			Scanner fileScanner = new Scanner(new File(DATA_FILE));
			// First read is for headings:
			fileScanner.nextLine();
			// All subsequent reads are for billionaire data
			while (fileScanner.hasNextLine()) {
				String[] data = fileScanner.nextLine().split(",");
				// Length of values is one less than field names because values
				// does not have id (DB will assign one)
				String[] values = new String[FIELD_NAMES.length - 1];
				values[0] = data[12].replaceAll("'", "''");
				values[1] = data[0];
				values[2] = data[9];
				values[3] = data[20];
				values[4] = data[2];
				values[5] = data[16];
				values[6] = ((data[18].equalsIgnoreCase("true")) ? "1" : "0");
				theOne.mDB.createRecord(Arrays.copyOfRange(FIELD_NAMES, 1, FIELD_NAMES.length), values);
				recordsCreated++;
			}

			// All done with the CSV file, close the connection
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return recordsCreated;
	}
}
