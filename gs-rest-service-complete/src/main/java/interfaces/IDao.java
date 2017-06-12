package interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import entities.AutotopVersion;
import entities.Command;
import entities.ReturnCode;

public interface IDao {

	public void endProcess();

	Map<Integer, List<Object>> fetchRows(String dbFile, String filterStmnt, int pageNo) throws SQLException;

	public Map<Integer, List<Object>> getServerCurrentVersion(String string) throws SQLException;

	public ReturnCode insertNewVersion(String string, String string2);

	public String getVersionServerName();

	public ReturnCode insertNewCommand(Command cmd);

	public ReturnCode updateCommand(String serverName, int comandNo, String updateString);

	public List<Command> getServerComands(String serverName) throws SQLException;

	public AutotopVersion getServerVersion(String serverName) throws SQLException;

}
