package entities;

public class Command {
	private String serverName;
	private String command;
	private int commandNo;
	private String user;
	private int status;
	private int errorCode;
	private String message;

	private String authentication;

	public Command(String serverName, int commandNo, String command, String user, int status, int errorCode,
			String message, String authentication) {

		this.serverName = serverName;
		this.command = command;
		this.user = user;
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
		this.authentication = authentication;
		this.commandNo = commandNo;
	}

	public Command() {

	}

	public int getCommandNo() {
		return commandNo;
	}

	public void setCommandNo(int commandNo) {
		this.commandNo = commandNo;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

}
