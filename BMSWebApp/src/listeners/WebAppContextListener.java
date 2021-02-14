package listeners;

import constants.Constants;
import engine.engine.BMSEngine;
import engine.engine.Engine;
import engine.users.ActiveUsersManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBException;

@WebListener("WebApp Context Listener")
public class WebAppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        BMSEngine engine = new Engine();
        context.setAttribute(Constants.ENGINE_ATTRIBUTE_NAME, engine);
        ActiveUsersManager activeUsersManager = new ActiveUsersManager();
        engine.addAdminUserForProgramAccess();
        activeUsersManager.addUsername(engine.getMemberPerUsername("admin@gmail.com").getEmailAddress());

        context.setAttribute(Constants.ACTIVE_USER_MANAGER_NAME, activeUsersManager);

        //load engineState
    }

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
