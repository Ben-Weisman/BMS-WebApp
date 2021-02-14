package utils;

import constants.Constants;
import engine.engine.BMSEngine;
import engine.users.ActiveUsersManager;

import javax.servlet.ServletContext;

public class ServletUtils {
    public static BMSEngine getEngine(ServletContext servletContext){
        return (BMSEngine) servletContext.getAttribute(Constants.ENGINE_ATTRIBUTE_NAME);
    }

    public static ActiveUsersManager getActiveUserManager(ServletContext servletContext) {
        return (ActiveUsersManager) servletContext.getAttribute(Constants.ACTIVE_USER_MANAGER_NAME);
    }
}
