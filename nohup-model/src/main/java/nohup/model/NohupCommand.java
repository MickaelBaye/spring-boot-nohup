package nohup.model;

import org.slf4j.LoggerFactory;

/**
 * TODO documentation
 * Created by mibaye on 17/08/2016.
 */
public class NohupCommand {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NohupCommand.class);

    /**
     * TODO documentation
     * @param process
     * @return
     * @throws Exception
     */
    public NohupProcess execute(NohupProcess process) throws Exception {

        Thread thread = new Thread(process);

        thread.start();
        process.setThread(thread);

        LOGGER.info(process.toString());

        return process;
    }
}
