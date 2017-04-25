/**
 *
 */
package edu.orangecoastcollege.cs272.ic11.test;

import edu.orangecoastcollege.cs272.ic11.model.DBModel;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Arrays;



import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
/**
 * @author sbadajoz
 *
 */
public class TestDBModel {
	private static final String DB_NAME = "cs272_test.db";
	private static final String TABLE_NAME = "billionaire";
	private static final String[] FIELD_NAMES = { "id", "name", "age", "gender", "worth", "citizenship", "sector", "political" };
	private static final String[] FIELD_TYPES = { "INTEGER PRIMARY KEY", "TEXT", "INTEGER", "TEXT", "REAL", "TEXT", "TEXT", "INTEGER" };
	private static final String DATA_FILE = "billionaires.csv";

	private static DBModel db;

	private String[] values;
	/**
	 * Defines variables, resources, etc.
	 * Executes once before all testing begins
	 * Does anything needed before testing
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		db = new DBModel(DB_NAME, TABLE_NAME, FIELD_NAMES, FIELD_TYPES);
	}

	/**
	 * Cleans up any open resources
	 * Executes once after all testing has completed
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.close();
	}

	/**
	 * Executed before each individual test
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		values = new String[] {"1", "Test Person", "100", "male", "2.5", "United States", "churros", "1"};
	}

	/**
	 * Executed after each individual test
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		db.deleteAllRecords();
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#getAllRecords()}.
	 */
	@Test
	public void testGetAllRecords() {
		try {
			db.getAllRecords();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Getting all records on empty database should not generate an SQLException");
		}
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#getRecord(java.lang.String)}.
	 */
	@Test
	public void testGetRecord() {
		try {
			db.getRecord(values[0]);
		} catch (SQLException e) {
			fail("Getting record on empty database should not generate SQLException." + e.getMessage());
		}

		try {
			db.createRecord(FIELD_NAMES, values);
			db.getRecord(values[0]);
		}
		catch (SQLException e) {
			fail("Getting record on non-empty database should not generate SQLException. " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#getRecordCount()}.
	 */
	@Test
	public void testGetRecordCount() {
		try {
			db.getRecordCount();
		} catch (SQLException e) {
			fail("Getting record count should not generate an SQLException.");
		}
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#createRecord(java.lang.String[], java.lang.String[])}.
	 */
	@Test
	public void testCreateRecord() {
		try {
			assertTrue("Testing creation of billionaire with id provided", db.createRecord(FIELD_NAMES, values));
			assertEquals("Testing the count of records", 1, db.getRecordCount());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail("Creation of records should not generate an SQLException");
		}

		try {
			db.createRecord(FIELD_NAMES, values);
			fail("Creating a record with duplicate id should generate a SQLException.");
		}
		catch(SQLException e) {
		}
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#updateRecord(java.lang.String, java.lang.String[], java.lang.String[])}.
	 */
	@Test
	public void testUpdateRecord() {
		try {
			db.updateRecord(values[0], FIELD_NAMES, values);
		} catch (SQLException e) {
			fail("Updating a record should not generate an SQLException.");
		}
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#deleteAllRecords()}.
	 */
	@Test
	public void testDeleteAllRecords() {
		try {
			db.createRecord(Arrays.copyOfRange(FIELD_NAMES, 1, FIELD_NAMES.length), Arrays.copyOfRange(values, 1, values.length));
			db.deleteAllRecords();
			assertEquals("Count after deletion should be 0.", 0, db.getRecordCount());
		} catch (SQLException e) {
			e.getMessage();
			fail("Deleteion should not genereate an SQLException");
		}
	}

	/**
	 * Test method for {@link edu.orangecoastcollege.cs272.ic11.model.DBModel#deleteRecord(java.lang.String)}.
	 */
	@Test
	public void testDeleteRecord() {
		try {
			db.createRecord(Arrays.copyOfRange(FIELD_NAMES, 1, FIELD_NAMES.length), Arrays.copyOfRange(values, 1, values.length));
			assertTrue("Before deletion, count should be positive", db.getRecordCount() > 0);
			db.deleteRecord(values[0]);
			assertEquals("Count after deletion should be 0.", 0, db.getRecordCount());
		}
		catch (SQLException e) {
			fail("Deletion should not generate an SQLException.");
		}
	}

}
