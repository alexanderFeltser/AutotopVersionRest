package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import controller.AutotopProperties;
import entities.AutotopVersion;
import entities.Command;
import entities.ReturnCode;
import interfaces.IDao;

@Configuration
public class DbExecutor implements IDao {

	String url, driver, user, pass, fileSource, fileTarget = "";
	int batchProceededRows = 0;
	int totalFetchedRows = 0;
	static List<Driver> drivers = new ArrayList<Driver>();
	Connection connection = null;

	final String SQL_SELECT_FROM_TABLE = "Select * from hfrf.";
	final String SQL_COUNT_FROM_TABLE = "Select count(*) from ";

	@Autowired
	public DbExecutor(AutotopProperties prop) throws Exception {
		this(prop.getDbProp().getProperty("url"), prop.getDbProp().getProperty("driver"),
				prop.getDbProp().getProperty("user"), prop.getDbProp().getProperty("pass"),
				Integer.parseInt(prop.getDbProp().getProperty("batchProceededRows")));
	}

	public DbExecutor(String url, String driver, String user, String pass, int batchProceededRows) throws Exception {

		this.batchProceededRows = batchProceededRows;
		try {
			Class.forName(driver);
			drivers.add(DriverManager.getDriver(url));
			for (Driver driverFound : drivers) {
				if (driverFound.acceptsURL(url)) {

					this.url = url;
					this.driver = driver;
					this.user = user;
					this.pass = pass;
					break;
				}
			}
			connection = getConnection();
		} catch (Exception exc) {
			System.out.println("User : " + this.user);
			System.out.println("Url : " + this.url);
			System.out.println("Connection Fault: " + exc.getMessage());
		}
	}

	@Override
	public ReturnCode insertNewVersion(String serverName, String version) {
		Statement stmt = null;
		ReturnCode retCode = null;
		int maxUpdateNo = getMaxServerUpdateNo(serverName);
		maxUpdateNo++;
		try {
			String query;
			query = "  INSERT INTO hfrf.hfpsrvrvrs ( srvr_name , update_no , version , date_time , update_usr )"
					+ " VALUES (" + surraundByQuotes(serverName) + ", " + maxUpdateNo + ", " + surraundByQuotes(version)
					+ ", now() , 'aplication') ";

			if (connection == null)
				connection = getConnection();
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			connection.commit();
			retCode = new ReturnCode(0, "Inserted ");

		} catch (Exception e) {
			retCode = new ReturnCode(1, e.getMessage());
			System.out.println(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();

			} catch (Exception e) {
			}
			;

		}
		return retCode;

	}

	private Connection getConnection() throws SQLException {

		Connection connect1 = DriverManager.getConnection(url, user, pass);
		connect1.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		connect1.setAutoCommit(false);
		return connect1;
	}

	private List<Object> getNextRecord(int columnsNumber, ResultSet rs) throws SQLException {
		List<Object> listObjects = new ArrayList<Object>(columnsNumber);
		for (int i = 1; i <= columnsNumber; i++) {
			listObjects.add(rs.getObject(i));
		}
		return listObjects;
	}

	@Override
	public Map<Integer, List<Object>> fetchRows(String tableName, String whereStmnt, int pageNo) throws SQLException {

		Statement stmt = null;
		ResultSet rs = null;

		/* Create Map for Excel Data */
		Map<Integer, List<Object>> excel_data = new HashMap<Integer, List<Object>>(); // create

		int mapRow_counter = 0;
		int startRowNo = 1;
		int countRows = 0;
		int endRowNo = startRowNo + batchProceededRows - 1;
		if (pageNo > 1) {
			startRowNo = batchProceededRows * pageNo - batchProceededRows;
			endRowNo = startRowNo + batchProceededRows;
		}

		try {

			if (connection == null)
				connection = getConnection();
			stmt = connection.createStatement();
			String s = SQL_SELECT_FROM_TABLE + tableName + whereStmnt;
			rs = stmt.executeQuery(SQL_SELECT_FROM_TABLE + tableName + whereStmnt);
			ResultSetMetaData rsmd = rs.getMetaData();

			int columnsNumber = rsmd.getColumnCount();

			while (rs.next()) {
				countRows++;

				if (countRows > endRowNo) {
					break;
				}

				if (countRows < startRowNo) {
					continue;
				}

				mapRow_counter++;
				totalFetchedRows++;
				excel_data.put(mapRow_counter, getNextRecord(columnsNumber, rs));
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			;
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			;

		}
		return excel_data;
	}

	private String surraundByQuotes(String serverName) {

		return "\"" + serverName + "\"";
	}

	void printResultSet(ResultSetMetaData rsmd, ResultSet rs) throws SQLException {

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {

			if (i > 1)
				System.out.print(",  ");
			String columnValue = rs.getString(i);
			System.out.print(columnValue + " " + rsmd.getColumnName(i));
			System.out.println("");
		}
	}

	@Override
	public void endProcess() {

		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		;

	}

	private int getMax(String schemaTable, String maxColumn, String whereExpresion) {
		ResultSet rs = null;
		Statement stmt = null;
		String queryString;
		int maxColumnValue = -1;
		try {
			if (connection == null)
				connection = getConnection();
			queryString = "Select max(" + maxColumn + ") from " + schemaTable + " where " + whereExpresion;
			stmt = connection.createStatement();

			rs = stmt.executeQuery(queryString);
			rs.next();
			maxColumnValue = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			;
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		return maxColumnValue;
	}
	///////////////////////////////// work with version tables //////////////

	@Override
	public Map<Integer, List<Object>> getServerCurrentVersion(String serverName) throws SQLException {
		String where;
		int maxUpdateNo = getMaxServerUpdateNo(serverName);

		where = " where update_no = " + maxUpdateNo;
		where = where + " and srvr_name = " + surraundByQuotes(serverName);
		Map<Integer, List<Object>> m = fetchRows("hfpsrvrvrs", where, 0);
		return m;
	}

	private int getMaxServerUpdateNo(String serverName) {
		ResultSet rs = null;
		Statement stmt = null;
		String queryString;
		int updateNo = -1;
		try {
			if (connection == null)
				connection = getConnection();
			queryString = "Select max(update_no) from hfpsrvrvrs where srvr_name =" + surraundByQuotes(serverName);
			stmt = connection.createStatement();

			rs = stmt.executeQuery(queryString);
			rs.next();
			updateNo = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			;
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		return updateNo;
	}

	@Override
	public String getVersionServerName() {

		String where;

		where = "  where vrsn_srvr = 1 ";

		Map<Integer, List<Object>> m = null;
		try {
			m = fetchRows("hfpsrvrinf", where, 0);
		} catch (Exception e) {
			System.out.println("Error : Version Server not found in table hfrf.hfpsrvrinf");
			e.printStackTrace();
		}
		return (String) m.get(1).get(1);

	}

	@Override
	public AutotopVersion getServerVersion(String serverName) throws SQLException {
		String where;
		AutotopVersion version = null;
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Map<Integer, List<Object>> m = getServerCurrentVersion(serverName);
			version = new AutotopVersion(serverName, (Integer) m.get(1).get(1), (String) m.get(1).get(2),
					sdfDate.format(m.get(1).get(3)), (String) m.get(1).get(4));
		} catch (Exception e) {
			System.out.println("Error : Version of Server " + serverName + "not found in table hfrf.hfpsrvrinf: "
					+ e.getMessage());
			// e.printStackTrace();
		}

		return version;
	}

	///////////////////////////////// work with comands tables //////////////

	@Override
	public ReturnCode insertNewCommand(Command cmd) {
		Statement stmt = null;
		ReturnCode retCode = null;
		int maxUpdateNo = getMax("hfrf.commands", "command_no", "srvr_name = " + surraundByQuotes(cmd.getServerName()));
		maxUpdateNo++;
		try {

			String query;
			query = "  INSERT INTO hfrf.commands(srvr_name,command_no,comman,user,status,err_code,message,"
					+ "date_time) VALUES (" + surraundByQuotes(cmd.getServerName()) + ", " + maxUpdateNo + ", "
					+ surraundByQuotes(cmd.getCommand()) + ", " + surraundByQuotes(cmd.getUser()) + ", " + 0 + ", " + 0
					+ ", \"\" ," + "now() )";
			System.out.println("Add new comand: " + query);

			if (connection == null)
				connection = getConnection();
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			connection.commit();
			retCode = new ReturnCode(0, "Inserted ");

		} catch (Exception e) {
			retCode = new ReturnCode(1, e.getMessage());
			System.out.println(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();

			} catch (Exception e) {
			}
			;

		}
		return retCode;

	}

	@Override
	public ReturnCode updateCommand(String serverName, int comandNo, String updateString) {
		Statement stmt = null;
		ReturnCode retCode = null;

		try {

			String query;
			query = "  update  hfrf.commands set " + updateString + " where command_no =  " + comandNo;

			if (connection == null)
				connection = getConnection();
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			connection.commit();
			retCode = new ReturnCode(0, "Comand Updated ");

		} catch (Exception e) {
			retCode = new ReturnCode(1, e.getMessage());
			System.out.println(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();

			} catch (Exception e) {
			}
			;

		}
		return retCode;

	}

	@Override
	public List<Command> getServerComands(String serverName) throws SQLException {

		String where;

		where = " where srvr_name = " + surraundByQuotes(serverName) + "and status = 0 " + " order by command_no";

		Map<Integer, List<Object>> m = fetchRows("commands", where, 0);
		List<Command> commands = new ArrayList<Command>();
		Set<Integer> rows = m.keySet();
		for (Integer row : rows) {
			List<Object> l = m.get(row);
			System.out.println(l);
			Command cmd = new Command((String) l.get(0), // ServerName
					(Integer) l.get(1), // comand_no
					(String) l.get(2), // command
					(String) l.get(3), // user
					Integer.parseInt((String) l.get(4)), // status
					((BigDecimal) l.get(5)).intValue(), // errorCode
					(String) l.get(6), // message,
					"");// authentication)
			commands.add(cmd);
		}

		return commands;
	}

}

// @Override
// public int getRowsFetched() {
//
// return totalFetchedRows;
// }

// @Override
// public List<Integer> getColumnTypes(FileImport dbFile) throws
// SQLException {
//
// Statement stmt = null;
// ResultSet rs = null;
// List<Integer> listMetaDataType = null;
//
// try {
// if (connection == null || connection.isClosed())
// connection = getConnection();
// stmt = connection.createStatement();
// rs = stmt.executeQuery(SQL_SELECT_FROM_TABLE + dbFile.getFileName());
// ResultSetMetaData rsmd = rs.getMetaData();
// listMetaDataType = new ArrayList<Integer>(rsmd.getColumnCount());
// for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//
// int typeIndex = rsmd.getColumnType(i);
//
// listMetaDataType.add(rsmd.getColumnType(i));
// rsmd.getPrecision(i);
// }
// } catch (Exception e) {
// System.out.println(e);
// } finally {
// try {
// if (rs != null)
// rs.close();
// } catch (Exception e) {
// }
// ;
// try {
// if (stmt != null)
// stmt.close();
// } catch (Exception e) {
// }
// ;
//
// }
// return listMetaDataType;
// }

// @Override
// public List<Integer> getPrecisionSize(FileImport dbFile) throws
// SQLException {
//
// List<Integer> listPrecisionSize = null;
// Statement stmt = null;
// ResultSet rs = null;
//
// try {
// if (connection == null || connection.isClosed())
// connection = getConnection();
// stmt = connection.createStatement();
// rs = stmt.executeQuery(SQL_SELECT_FROM_TABLE + dbFile.getFileName());
// ResultSetMetaData rsmd = rs.getMetaData();
//
// listPrecisionSize = new ArrayList<Integer>(rsmd.getColumnCount());
// for (int i = 1; i <= rsmd.getColumnCount(); i++) {
// listPrecisionSize.add(rsmd.getPrecision(i));
// }
//
// } catch (Exception e) {
// System.out.println(e);
// } finally {
// try {
// if (rs != null)
// rs.close();
// } catch (Exception e) {
// }
// ;
// try {
// if (stmt != null)
// stmt.close();
// } catch (Exception e) {
// }
// ;
//
// }
// return listPrecisionSize;
// }

// @Override
// public int geTotalRows(FileImport dbFile) throws SQLException {
// int totalRows = 0;
//
// Statement stmt = null;
// ResultSet rs = null;
//
// try {
// if (connection == null || connection.isClosed())
// connection = getConnection();
// stmt = connection.createStatement();
// rs = stmt.executeQuery(SQL_SELECT_FROM_TABLE + dbFile.getFileName());
// while (rs.next()) {
// totalRows = rs.getInt(1);
// }
// } catch (Exception e) {
// System.out.println(e);
// } finally {
// try {
// if (rs != null)
// rs.close();
// } catch (Exception e) {
// }
// ;
// try {
// if (stmt != null)
// stmt.close();
// } catch (Exception e) {
// }
// ;
//
// }
//
// return totalRows;
// }

// @Override
// public List<String> getColumnNames(FileImport dbFile) {
//
// Statement stmt = null;
// ResultSet rs = null;
// ArrayList<String> listColumnNames = null;
//
// try {
// if (connection == null)
// connection = getConnection();
// stmt = connection.createStatement();
// rs = stmt.executeQuery(SQL_SELECT_FROM_TABLE + dbFile.getFileName());
// ResultSetMetaData rsmd = rs.getMetaData();
//
// listColumnNames = new ArrayList<String>(rsmd.getColumnCount());
//
// for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//
// listColumnNames.add(rsmd.getColumnName(i));
// rsmd.getPrecision(i);
// }
// } catch (Exception e) {
// System.out.println(e);
// } finally {
// try {
// if (rs != null)
// rs.close();
// } catch (Exception e) {
// }
// ;
// try {
// if (stmt != null)
// stmt.close();
// } catch (Exception e) {
// }
// ;
//
// }
// return listColumnNames;
// }
