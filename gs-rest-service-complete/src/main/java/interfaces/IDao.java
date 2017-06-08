package interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import entities.ReturnCode;

public interface IDao {

	public void endProcess();

	Map<Integer, List<Object>> fetchRows(String dbFile, String filterStmnt, int pageNo) throws SQLException;

	public Map<Integer, List<Object>> getServerCurrentVersion(String string) throws SQLException;

	public ReturnCode insertNewVersion(String string, String string2);

	public String getVersionServerName();

}
