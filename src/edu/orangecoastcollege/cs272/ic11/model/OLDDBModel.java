package edu.orangecoastcollege.cs272.ic11.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OLDDBModel {

	private static final String DATA_FILE = "billionaires.csv";
	
	public static Connection connectToDB(String dbName) throws ClassNotFoundException, SQLException {
		// Load SQLite database classes
		Class.forName("org.sqlite.JDBC");
		// Establish a connection to the database and return that connection
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		// Populate DB with data from CSV file (if needed)
		populateDBFromFile(connection, DATA_FILE);
		return connection;
	}

	public static ResultSet getAllRecords(Connection connection)
	{
		ResultSet rs = null;		
		try
		{
			Statement stmt = connection.createStatement();
			stmt.setQueryTimeout(30);
			rs = stmt.executeQuery("SELECT * from billionaire");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}		
		return rs;
	}
	
	private static int populateDBFromFile(Connection connection, String fileName) {
		int recordsCreated = 0;
		
		try {
			Statement stmt = connection.createStatement();
			stmt.setQueryTimeout(30);			
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS billionaire (" 
					+ "id INTEGER PRIMARY KEY, "
					+ "name TEXT," 
					+ "age INTEGER," 
					+ "gender TEXT," 
					+ "worth REAL," 
					+ "citizenship TEXT,"
					+ "sector TEXT," 
					+ "political INTEGER)");

			// Let's query the table to see if it has pre-existing data
			// Queries return what are called ResultSet (set of results)
			ResultSet rs = stmt.executeQuery("SELECT * FROM billionaire");
			// If the result set contains results, database table already has
			// records, no need to populate from file (so return false)
			if (rs.next())
				return 0;

			// Otherwise, open the file (CSV file) and insert billionaire data
			// into database
			Scanner fileScanner = new Scanner(new File(fileName));
			// First read is for headings:
			fileScanner.nextLine();
			// All subsequent reads are for billionaire data
			while (fileScanner.hasNextLine()) {
				String[] data = fileScanner.nextLine().split(",");
				String name = data[12].replaceAll("'", "''");
				String age = data[0];
				String gender = data[9];
				String worth = data[20];
				String citizenship = data[2];
				String sector = data[16];
				int political = ((data[18].equalsIgnoreCase("true")) ? 1 : 0);

				String insertStmt = 
						"INSERT INTO billionaire (name,age,gender,worth,citizenship,sector,political) "
								+ "VALUES('" 
								+ name + "'," 
								+ age + ",'" 
								+ gender + "',"
								+ worth + ",'" 
								+ citizenship + "','" 
								+ sector + "'," 
								+ political + ")";
				//System.out.println(insertStmt);
				stmt.executeUpdate(insertStmt);
				recordsCreated++;
			}


			// All done with the database, close the connection
			fileScanner.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return recordsCreated;
	}

}
