package main;

import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import entities.Log4J2XmlConf;

@SpringBootApplication
@ComponentScan(basePackages = { "controller", "interfaces", "dao", "helper" }, basePackageClasses = Application.class)
public class Application {

	public static void main(String[] args) {
		Logger logger = Log4J2XmlConf.getLogger();
		logger.info("Autotop Version Server starting");
		SpringApplication.run(Application.class, args);
	}
}
