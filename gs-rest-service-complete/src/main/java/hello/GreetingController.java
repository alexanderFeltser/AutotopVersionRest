package hello;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.AutotopProperties;
import controller.ServerManeger;
import entities.AutotopVersion;
import entities.ReturnCode;
import entities.Url;

@RestController
public class GreetingController {
	@Autowired
	private AutotopProperties prop;
	@Autowired
	private ServerManeger srverManeger;
	private static final String template = "Hello %s time, %s!";
	private static final String templateMorning = "Доброе утро %s раз, %s!";
	private static final String templateKiss = "Целую тебя  %s раз, %s!";
	private final AtomicLong counter = new AtomicLong();

	// @RequestMapping(value = "/gotversion", method = RequestMethod.POST)
	// public void gotNewvVersion(@RequestParam(value = "servername") String
	// serverName,
	// @RequestParam(value = "versionname") String versionName) {
	// srverManeger.postServerVersion(serverName, versionName);
	// }

	@RequestMapping(value = "/gotversion", method = RequestMethod.POST)
	public ResponseEntity<Url> gotNewvVersion(@RequestBody AutotopVersion autotopVersion) {
		// System.out.println("Start gotNewvVersion" + autotopVersion);
		ReturnCode r = srverManeger.postServerVersion(autotopVersion.getServerName(), autotopVersion.getVersion());
		if (r.isError()) {
			return new ResponseEntity<Url>((new Url(r.getErrorMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<Url>(new Url("/gotversion"), HttpStatus.OK);
		}
	}

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/autotopVersion")

	public ResponseEntity<AutotopVersion> autotopVersion(@RequestParam(value = "serverNname") String serverNname) {
		Date date = new Date(System.currentTimeMillis());
		AutotopVersion a = new AutotopVersion("Autotp-New1", "engine:2.5.8.0,pb:23.14,DB:3235", "Eti");
		// AutotopVersion a = new AutotopVersion();
		// a.setServerName("Autotp-New1");
		// a.setVersion("engine:2.5.8.0,pb:23.14,DB:3235");
		// a.setUpdateUser("Eti");
		return new ResponseEntity<AutotopVersion>(a, HttpStatus.OK);
	}

	@RequestMapping("/chmok")
	public @ResponseBody String jopa(@RequestParam(value = "name", defaultValue = "World") String name) {
		counter.incrementAndGet();
		return String.format(templateKiss, counter.toString(), name);
	}

	@RequestMapping("/morning")
	public @ResponseBody String morning(@RequestParam(value = "name", defaultValue = "World") String name) {
		counter.incrementAndGet();
		System.out.println(prop.getDbProp().getProperty("url"));
		return String.format(templateMorning, counter.toString(), name);
	}

	@RequestMapping("/kuku")
	public @ResponseBody String kuku(@RequestParam(value = "name", defaultValue = "World") String name) {

		return String.format("How are you %s ? ", name);
	}

	@RequestMapping("/date")
	public @ResponseBody String date(@RequestParam(value = "name", defaultValue = "World") String name) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println(dateFormat.format(date));

		return String.format("The Time is %s", dateFormat.format(date));
	}

	/////////////////////////////////////////////////////////////////////

}
