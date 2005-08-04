package org.isb.xmlrpc.handler.db;

import java.sql.*;
import java.util.*;
import java.lang.*;

/**
 * A handler for any SQL based database, it requires a fully described database
 * URL of the form jdbc:<subprotocol>:<subname>, and a JDBC driver for the
 * specific database.
 * 
 * @author <a href="mailto:iavila@systemsbiology.org">Iliana Avila-Campillo</a>
 */
public class SQLDBHandler implements DBHandler {

	public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	protected boolean driverIsLoaded = false;
	protected String url;
	protected Connection connection;
	protected boolean debug = false;

	/**
	 * The database url can be set at a later time
	 * 
	 * @param jdbc_driver
	 *            the driver for the specific database technology (e.g. for a
	 *            mySQL database, "com.mysql.jdbc.Driver")
	 */
	public SQLDBHandler(String jdbc_driver) {
		this(null, jdbc_driver);
	}

	/**
	 * 
	 * @param db_url
	 *            a fully specified jdbc database URL, for example,
	 *            "jdbc:mysql://biounder.kaist.ac.kr/prolinks"
	 * @param jdbc_driver
	 *            the driver for the specific database technology (e.g. for a
	 *            mySQL database, "com.mysql.jdbc.Driver")
	 * @param connection_props
	 *            a Hashtable with properties for creating the db connection
	 */
	public SQLDBHandler(String db_url, String jdbc_driver,
			Hashtable connection_props) {
		this.driverIsLoaded = loadDriver(jdbc_driver);
		if (this.driverIsLoaded) {
			makeConnection(db_url, connection_props);
		}// driver loaded
	}

	/**
	 * 
	 * @param db_url
	 *            a fully specified jdbc database URL, for example,
	 *            "jdbc:mysql://biounder.kaist.ac.kr/prolinks"
	 * @param jdbc_driver
	 *            the driver for the specific database technology (e.g. for a
	 *            mySQL database, "com.mysql.jdbc.Driver")
	 */
	public SQLDBHandler(String db_url, String jdbc_driver) {
		this.driverIsLoaded = loadDriver(jdbc_driver);
		if (this.driverIsLoaded) {
			makeConnection(db_url);
		}// driver loaded
	}

	/**
	 * Looks for a class with the given driver name (Class.forName), and creates
	 * a new instance for it
	 * 
	 * @param jdbc_driver
	 * @return true if the driver was loaded, false otherwise
	 */
	protected boolean loadDriver(String jdbc_driver) {
		boolean loaded = true;

		try {
			// load the JDBC driver
			Class.forName(jdbc_driver).newInstance();
			loaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			loaded = false;
		}

		return loaded;
	}

	/**
	 * 
	 * @param db_url
	 *            URL of the form jdbc:<subprotocol>:<subname>
	 * @return true if the connection was successful, false otherwise
	 */
	public boolean makeConnection(String db_url) {
		if (db_url == null) {
			return false;
		}
		this.url = db_url;

		// In case that there is already a connection:
		shutdown();

		try {
			// Make a connection to the db
			this.connection = DriverManager.getConnection(this.url);
			// Make sure the connection is OK
			if (!this.connection.isClosed()) {
				System.out.println("Successfully connected to mySQL database "
						+ this.url);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param db_url
	 *            URL of the form jdbc:<subprotocol>:<subname>
	 * @param props
	 *            a set of properties to make the connection (they will pupulate
	 *            a Properties object)
	 * @return true if the connection was successful, false otherwise
	 */
	public boolean makeConnection(String db_url, Hashtable props) {
		if (db_url == null) {
			return false;
		}
		this.url = db_url;

		// In case that there is already a connection:
		shutdown();

		try {
			Properties properties = new Properties();
			properties.putAll(props);
			// Make a connection to the db
			this.connection = DriverManager.getConnection(this.url, properties);
			// Make sure the connection is OK
			if (!this.connection.isClosed()) {
				System.out.println("Successfully connected to mySQL database "
						+ this.url);
				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return the URL for this db
	 */
	public String getURL() {
		return this.url;
	}// getURL
	
	/**
	 * Sets the database URL, and makes a new connection to it
	 *
	 * @param db_url the database URL
	 * @param props a list of arbitrary string tag/value pairs as connection arguments; 
	 * normally at least a "user" and "password" property should be included
	 */
	public void setURL (String db_url, Hashtable props){
		makeConnection(db_url, props);
	}	

	/**
	 * Closes the connection to the DB
	 */
	public void shutdown() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}// shutdown

	/**
	 * @return the status of this DB (CLOSED, OPEN)
	 */
	public String getStatus() {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				return DBHandler.CLOSED;
			}
		} catch (SQLException e) {
			// Database access error
			e.printStackTrace();
			// TODO: Have a ACCESS_ERROR return value???
			return DBHandler.CLOSED;
		}
		return DBHandler.OPEN;
	}// getStatus

	/**
	 * Updates the data in the database
	 * 
	 * @param props
	 *            a list of arbitrary string tag/value pairs as connection
	 *            arguments; normally at least a "user" and "password" property
	 *            should be included
	 */
	public boolean update(Hashtable props) {
		return false; // for now
	}// update

	/**
	 * @return whether or not debugging print statements should be output
	 */
	public boolean getDebug() {
		return this.debug;
	}// getDebug

	/**
	 * Sets whether or not debugging print statements should be output
	 */
	public void setDebug(boolean d) {
		this.debug = d;
	}// setDebug

}