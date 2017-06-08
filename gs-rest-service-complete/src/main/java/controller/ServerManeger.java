package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import entities.ReturnCode;
import interfaces.IDao;

@Component
public class ServerManeger {
	@Autowired
	private AutotopProperties prop;
	@Autowired
	// @Qualifier("DbExecutor")
	private IDao dao;

	public ReturnCode postServerVersion(String serverName, String versionName) {
		return dao.insertNewVersion(serverName, versionName);
	}
}
