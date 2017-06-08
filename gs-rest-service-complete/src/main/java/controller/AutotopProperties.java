package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AutotopProperties {

	private FileInputStream fis;
	private Properties prop;

	public Properties getDbProp() {
		return prop;
	}

	public AutotopProperties() {
		try {

			File fl = new File("Resources\\dbProp.xml");
			if (!fl.exists()) {
				System.out.println("File Resources\\dbProp.xml  does not exists");
				System.exit(0);
			}
			fis = new FileInputStream(fl);
			prop = new Properties();
			prop.loadFromXML(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Resources\\dbProp.xml  does not exists");
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Warning: Unable to close  Resources\\dbProp.xml  file.");
			System.out.println(e.getMessage());
		}
	}

}
