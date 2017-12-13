package me.igorek536.tasksim;

import me.igorek536.tasksim.gui.GuiFrame;
import me.igorek536.tasksim.gui.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("FieldCanBeLocal")
public class Launcher {

    private final Logger logger = LoggerFactory.getLogger("Launcher");
    private final Runtime runtime = Runtime.getRuntime();

    Launcher() {
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Goodbye...");
            }
        });
    }

    void launch() {
        GuiFrame maimFrame = new MainFrame();
        logger.debug("Launching...");
        maimFrame.init();
    }
}
