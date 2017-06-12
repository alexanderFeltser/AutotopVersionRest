package entities;

public class AutotopVersion {
	private String serverName;
	private String updateDateTime;
	private int updateNo;
	private String version;
	private String updateUser;

	public AutotopVersion() {
	}

	public AutotopVersion(String serverName, int updateNo, String version, String updateDateTime, String updateUser) {

		this.serverName = serverName;
		this.setUpdateDateTime(updateDateTime);
		this.version = version;
		this.updateUser = updateUser;
		this.updateNo = updateNo;
	}

	public int getUpdateNo() {
		return updateNo;
	}

	public void setUpdateNo(int updateNo) {
		this.updateNo = updateNo;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	// public Date getUpdateDateTime() {
	// return updateDateTime;
	// }
	//
	// public void setUpdateDateTime(Date updateDateTime) {
	// this.updateDateTime = updateDateTime;
	// }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	public String toString() {
		return "AutotopVersion [serverName=" + serverName + ", version=" + version + ", updateUser=" + updateUser + "]";
	}

	/**
	 * @return the updateDateTime
	 */
	public String getUpdateDateTime() {
		return updateDateTime;
	}

	/**
	 * @param updateDateTime
	 *            the updateDateTime to set
	 */
	public void setUpdateDateTime(String updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

}
