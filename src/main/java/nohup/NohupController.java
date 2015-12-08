package nohup;

import java.io.*;
import java.util.*;

import java.util.logging.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class NohupController {

	// Log
	private static Logger logger = Logger.getLogger("fr.personal.nohup.NohupController");
	private static FileHandler fileHandler;

	static {
		// Log configuration
		logger.setLevel(Level.ALL);
		try {
			fileHandler = new FileHandler("logs/NohupController.log", false);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		}
		catch (IOException ex) {
			logger.log(Level.WARNING, "Error during log file handler initialization", ex);
		}
	}

    @RequestMapping("/")
    public String index() {
        return "Greetings from Nohup!";
    }

    @RequestMapping("/firefox")
    public String firefox() {
    	String ret = "KO" ;

    	try {
    		// Java runtime
	    	Runtime runtime = Runtime.getRuntime();
	    	// Command
	    	String[] command = {"C:/Program Files (x86)/Mozilla Firefox/firefox.exe", "https://www.qwant.com/"};
	    	// Firefox Process
	    	Process process = runtime.exec(command);
	    	// Firefox output
	    	BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = input.readLine();
			ret = "OK\n";
			while (line != null) {
				ret += line;
				line = input.readLine();
			}
			logger.log(Level.INFO, "OK");
    	} catch (IOException ioe) {
    		logger.log(Level.SEVERE, "Failed to start Firefox", ioe);
    		ret = "KO" ;
    	}
	    	
        return ret;
    }

}