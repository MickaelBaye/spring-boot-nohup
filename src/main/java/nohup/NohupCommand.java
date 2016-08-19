package nohup;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mibaye on 17/08/2016.
 */
public class NohupCommand {

    private static Logger logger = Logger.getLogger(NohupCommand.class.getName());

    public NohupProcess execute(NohupProcess process) throws Exception {

        Thread thread = new Thread(process);

        thread.start();
        process.setThread(thread);

        logger.log(Level.INFO, process.toString());

        return process;
    }
}
