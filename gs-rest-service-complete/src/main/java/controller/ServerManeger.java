package controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import entities.Command;
import entities.ReturnCode;
import interfaces.AuthenticationAlgorithm;
import interfaces.IDao;

@Component
public class ServerManeger {
	@Autowired
	private AutotopProperties prop;
	@Autowired
	// @Qualifier("DbExecutor")
	private IDao dao;
	@Autowired
	private AuthenticationAlgorithm authentication;

	public ReturnCode postServerVersion(String serverName, String versionName) {
		return dao.insertNewVersion(serverName, versionName);
	}

	public ReturnCode addCommand(Command cmd) {

		if (isPassAuthentication(cmd.getAuthentication())) {

			return dao.insertNewCommand(cmd);
		} else {
			// System.out.println("addCommand :" + "Not pass Authentication");
			return new ReturnCode(1, "Error Wrong Authentication.");
		}
	}

	private boolean isPassAuthentication(String authenticationString) {

		return authentication.isPassAuthentication(authenticationString);
	}

	public ReturnCode commandExequted(String serverName, String commandNo) {
		try {
			return dao.updateCommand(serverName, Integer.parseInt(commandNo), " status = 3");
		} catch (NumberFormatException e) {
			return new ReturnCode(1, e.getMessage());

		}

	}

	public ReturnCode commandNotExequted(String serverName, String commandNo, String errorNo, String errorMessage) {
		try {
			String setOfUpdateStatement = " status = 1, err_code = " + Integer.parseInt(errorNo) + " ,message = '"
					+ errorMessage + "'";
			return dao.updateCommand(serverName, Integer.parseInt(commandNo), setOfUpdateStatement);
		} catch (NumberFormatException e) {
			return new ReturnCode(1, e.getMessage());

		}
	}

	public ReturnCode getServerComands(String serverName) {
		ReturnCode r;
		try {
			List<Command> command = dao.getServerComands(serverName);
			if (command.size() > 0) {
				r = new ReturnCode(0, "Fetched commands.");
				r.setObject(command);
			} else {
				r = new ReturnCode(0, "No commands found.");
			}
			return r;
		} catch (NumberFormatException e) {
			return new ReturnCode(1, e.getMessage());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return new ReturnCode(1, e.getMessage());
		}
	}
}
