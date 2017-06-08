package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import interfaces.IDao;

@Component
public class ServerManeger {
	@Autowired
	private AutotopProperties prop;
	@Autowired
	// @Qualifier("DbExecutor")
	private IDao dao;

	public void postServerVersion(String serverName, String versionName) {
		dao.insertNewVersion(serverName, versionName);
	}
}
