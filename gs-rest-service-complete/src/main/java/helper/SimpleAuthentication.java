package helper;

import org.springframework.stereotype.Component;

import interfaces.AuthenticationAlgorithm;

//@Configuration
@Component
public class SimpleAuthentication implements AuthenticationAlgorithm {

	@Override
	public boolean isPassAuthentication(String s) {

		String delims = "[:]";
		String[] tokens = s.split(delims);
		return (tokens[0].length() + tokens[1].length() == 13);
		// && tokens[1].substring(
		// tokens[1].length() - 3, tokens[1].length() -
		// 1).toUpperCase().equals("DBA"))

	}

}
