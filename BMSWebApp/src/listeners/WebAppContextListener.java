package listeners;

import constants.Constants;
import engine.classes.windows.ScheduleWindowBuilder;
import engine.classes.windows.ScheduleWindowDetails;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.InvalidInputException;
import engine.engine.BMSEngine;
import engine.engine.Engine;
import engine.users.ActiveUsersManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.time.LocalTime;

@WebListener("WebApp Context Listener")
public class WebAppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        BMSEngine engine = new Engine();
        context.setAttribute(Constants.ENGINE_ATTRIBUTE_NAME, engine);
        ActiveUsersManager activeUsersManager = new ActiveUsersManager();

        context.setAttribute(Constants.ACTIVE_USER_MANAGER_NAME, activeUsersManager);

        try {
            File file = new File("engineState.xml");
            if (file.exists()) {
                System.out.println(file.getAbsolutePath());
                engine.loadEngine(file);
                System.out.println("Engine state loaded successfully");
            }
            else{
                System.out.println("Couldn't find the engineState file in path 'server\\src\\engineState.xml'.");
            }
            engine.addAdminUserForProgramAccess();
            try {
                engine.addNewWindow(new ScheduleWindowDetails("shun",LocalTime.parse("20:00"),LocalTime.parse("21:00"),null));
                System.out.println("Window added successfully");
            } catch (ExportToXmlException e) {
                e.printStackTrace();
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }
            activeUsersManager.addUserID(engine.getMemberPerUsername("admin@gmail.com").getID());
        } catch (JAXBException e) {
            e.printStackTrace();
        }    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        BMSEngine engine = (BMSEngine) servletContextEvent.getServletContext().getAttribute(Constants.ENGINE_ATTRIBUTE_NAME);
        try {
            engine.saveEngineStateToXML();
        } catch (JAXBException e) {
            System.out.println("Error saving the current engine state to xml.");
        }
    }
}
