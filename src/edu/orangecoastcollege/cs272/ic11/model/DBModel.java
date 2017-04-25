package edu.orangecoastcollege.cs272.ic11.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <code>DBModel</code> represents the database model for a table containing one primary key
 * and one or more fields.  It provides mechanisms by which new records can be created and
 * existing ones can be updated or deleted.  <code>DBModel</code> also provides functionality
 * to query the database table for a single record, all records or the total count of records.
 *
 * @author
 * @version
 */
public class DBModel {

	private String mDBName;
	private String mTableName;
	private String[] mFieldNames;
	private String[] mFieldTypes;
	private Connection mConnection;
	private Statement mStmt;

	/**
	 * Instantiates a new <code>DBModel</code> given the required parameters, such as the database name,
	 * table name, field names and field types.
	 *
	 * @param dbName The database name, e.g. cs272.db
	 * @param tableName The table name, e.g. billionaire
	 * @param fieldNames The field names, e.g. "id", "name", "age", "gender", "worth", "citizenship", "sector", "political"
	 * @param fieldTypes The field types, e.g. "INTEGER PRIMARY KEY", "TEXT", "INTEGER", "TEXT", "REAL", "TEXT", "TEXT", "INTEGER"
	 * @throws SQLException If the field names are not the same length as the field types, or the names/types are empty,
	 * or there is an error connecting to the database.
	 */
	public DBModel(String dbName, String tableName, String[] fieldNames, String[] fieldTypes)
			throws SQLException {
		super();
		mDBName = dbName;
		mTableName = tableName;
		mFieldNames = fieldNames;
		mFieldTypes = fieldTypes;
		if (mFieldNames == null || mFieldTypes == null || mFieldNames.length == 0 || mFieldNames.length != mFieldTypes.length)
			throw new SQLException("Database field names and types must exist and have the same number of elements.");
		mConnection = connectToDB();
		mStmt = mConnection.createStatement();
		mStmt.setQueryTimeout(30);
		createTable();
	}

	/**
	 * Creates the database table, only if it does not already exist.
	 *
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	private void createTable() throws SQLException
	{
		StringBuilder createSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		createSQL.append(mTableName).append("(");
		for(int i = 0; i < mFieldNames.length; i++) {
			createSQL.append(mFieldNames[i]).append(" ").append(mFieldTypes[i]).append((i == mFieldNames.length - 1)?")":", ");
		}
		mStmt.executeUpdate(createSQL.toString());
	}

	/**
	 * Gets all records from the database.
	 *
	 * @return A <code>ResultSet</code> containing all records from the database table.
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	public ResultSet getAllRecords() throws SQLException {
		String selectSQL = "SELECT * FROM " + mTableName;
		return mStmt.executeQuery(selectSQL);
	}

	/**
	 * Gets a single record from the database matching a specific primary key.
	 *
	 * @param key The primary key value for the record to return.
	 * @return A <code>ResultSet</code> containing a single record matching the key.
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	public ResultSet getRecord(String key) throws SQLException {
		String selectSQL = "SELECT * FROM " + mTableName + " WHERE " + mFieldNames[0] + " = " + key;
		System.out.println(selectSQL);
		return mStmt.executeQuery(selectSQL);
	}

	/**
	 * Gets the count of all records in the database.
	 *
	 * @return The count of all records in the database.
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	public int getRecordCount() throws SQLException
	{
		int ret = 0;
		ResultSet rs = getAllRecords();
		while(rs.next())ret++;
		return ret;
	}

	/**
	 * Creates (inserts) a new record into the database with the fields and values provided.
	 * Usage: Do not provide a primary key in the fields or values, as the database will assign one automatically.
	 *
	 * @param fields The field names, e.g. "name", "age", "gender", "worth", "citizenship", "sector", "political"
	 * @param values The values, e.g. "Mike Paul", "40", "male", "2.2", "United States", "technology", "0"
	 * @return True if the record was created successfully, false if the fields length does not match the values length (or if fields/values are empty).
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	public boolean createRecord(String[] fields, String[] values) throws SQLException {
		if( fields == null || values == null || fields.length != values.length || fields.length == 0) return false;

		String[] fieldsOut = new String[mFieldNames.length];
		String[] valuesOut = new String[mFieldNames.length];
		int factor;

		if(!fields[0].equals("id")) {
			valuesOut[0] = "";
			factor = 1;
		}
		else {
//			System.out.println("FIELDS 0  = " + fields[0]);
			fieldsOut[0] = fields[0];
//			System.out.println("VALUES[0] = " + values[0]);
			factor = 0;
		}
		fieldsOut[0] = "id";
		for(int j = 0; j < mFieldNames.length - factor;j++) {
			if(factor + j > fields.length) {
				valuesOut[factor + j] = "";
				fieldsOut[factor + j] = mFieldNames[factor];
			}
			else if(mFieldNames[factor + j]!= fields[j]) {
				valuesOut[factor + j] = "";
				fieldsOut[factor + j] = mFieldNames[factor];
			}
			else {
				fieldsOut[factor + j] = fields[j];
				valuesOut[factor + j] = values[j];
			}

		}

////		TESTING
//		System.out.println("TESTING OLD");
//		System.out.print("FIELDS: ");
//		for (String s : fields) {
//			System.out.print(s + ", ");
//		}
//		System.out.println();
//		System.out.print("VALUES: ");
//		for (String s : values) {
//			System.out.print(s + ", ");
//		}
//		System.out.println();
//		System.out.println("\nEND TESTING");
//		System.out.println("TESTING NEW");
//		System.out.print("FIELDS: ");
//		for (String s : fieldsOut) {
//			System.out.print(s + ", ");
//		}
//		System.out.println();
//		System.out.print("VALUES: ");
//		for (String s : valuesOut) {
//			System.out.print(s + ", ");
//		}
//		System.out.println("\nEND TESTING");
////		TESTING END


		StringBuilder makeSQL = new StringBuilder("Insert INTO " + mTableName + "(");
		for(int i = factor; i < fieldsOut.length; i++) {
			makeSQL.append(fieldsOut[i]).append((i < fieldsOut.length - 1)?",":")");
		}
		makeSQL.append("VALUES(");
		for(int i = factor; i < fieldsOut.length;i++){
			makeSQL.append(convertToSQLText(fieldsOut[i], valuesOut[i])).append((i < valuesOut.length - 1)?",":")");
		}
//		System.out.println(makeSQL.toString());
		mStmt.executeUpdate(makeSQL.toString());
		return true;
	}

	/**
	 * Updates a single record from the database matching the given primary key value.
	 * Usage: Do not provide primary key in the fields or values, only provide it as the key parameter.
	 *
	 * @param key The primary key value to update.
	 * @param fields The field names, e.g. "name", "age", "gender", "worth", "citizenship", "sector", "political"
	 * @param values The values, e.g. "Mike Paul", "40", "male", "2.2", "United States", "technology", "0"
	 * @return True if the record was updated successfully, false if the fields length does not match the values length (or if fields/values are empty).
	 * @throws SQLException
	 */
	public boolean updateRecord(String key, String[] fields, String[] values) throws SQLException {
		if( fields == null || values == null || fields.length != values.length || fields.length == 0) return false;
		StringBuilder updateSQL = new StringBuilder("UPDATE ");
		updateSQL.append(mTableName).append(" ").append(" SET ");
		for (int i = 0; i < fields.length; i++) {
			updateSQL
					.append(fields[i])
					.append("=")
					.append(convertToSQLText(fields[i], values[i]))
					.append(i < fields.length - 1 ? "," : " WHERE ");
		}
		updateSQL.append(values[0]).append("=").append(key);
		mStmt.executeUpdate(updateSQL.toString());
		return true;
	}

	/**
	 * Deletes all records from the database.
	 *
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	public void deleteAllRecords() throws SQLException {
		String sql = "DELETE FROM " + mTableName + "()";
		mStmt.executeUpdate(sql);
	}

	/**
	 * Deletes a single record from the database matching the given primary key value.
	 *
	 * @param key The primary key value to delete.
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	public void deleteRecord(String key) throws SQLException {
		String sql = "DELETE FROM " + mTableName + " WHERE id = " + key;
		mStmt.executeUpdate(sql);
	}

	/**
	 * Converts a field value into SQL text by surrounding value with single quotes (e.g. technology becomes 'technology')
	 * This only happens when the field provided has the SQL data type TEXT.
	 *
	 * @param field The field name (e.g. sector)
	 * @param value The value (e.g. technology)
	 * @return The value surrounded with single quotes if it's field type is TEXT, otherwise returns the original value.
	 */
	private String convertToSQLText(String field, String value) {
		System.out.println("field: " + field + ", value: " + value);
		String dataType = "";
		for(int i = 1; i < mFieldNames.length; i++) {
			if(field.equals(mFieldNames[i])){
				dataType = mFieldTypes[i];
				break;
			}
		}
		//	 "INTEGER PRIMARY KEY", "TEXT", "INTEGER", "TEXT", "REAL", "TEXT", "TEXT", "INTEGER" };
		switch(dataType) {
			case "TEXT":
				return "'" + value + "'";
			case "INTEGER:":
				if(value.matches("[-+]?\\d*\\.?\\d+"))
					return value;
				return (value.toLowerCase().equals("true")?"1":"0");
			default:
				return value;
		}
	}

	/**
	 * Establishes a connection to the database.
	 *
	 * @return The connection to the database.
	 * @throws SQLException If a database access error occurs, this method is called on a closed Statement,
	 * or the given SQL statement produces anything other than a single ResultSet object.
	 */
	private Connection connectToDB() throws SQLException {
		// Load SQLite database classes
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Establish a connection to the database and return that connection
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + mDBName);
		return connection;
	}

	public void close() throws SQLException {
		mConnection.close();
	}
}
