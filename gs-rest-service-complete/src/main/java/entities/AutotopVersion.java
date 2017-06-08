package entities;

public class AutotopVersion {
	private String serverName;
	// private Date updateDateTime;
	private String version;
	private String updateUser;

	public AutotopVersion() {
	}

	public AutotopVersion(String serverName, String version, String updateUser) {

		this.serverName = serverName;
		// this.updateDateTime = updateDateTime;
		this.version = version;
		this.updateUser = updateUser;
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

}
