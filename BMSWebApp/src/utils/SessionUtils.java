package utils;

import com.sun.deploy.net.HttpRequest;
import constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute;
        if (session!=null)
            sessionAttribute = session.getAttribute(Constants.USERNAME);
        else  sessionAttribute = null;

        if (sessionAttribute != null)
            return sessionAttribute.toString();
        return null;
    }

    public static String getPassword(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute;
        if (session != null)
            sessionAttribute = session.getAttribute(Constants.PASSWORD);
        else sessionAttribute = null;

        if (sessionAttribute != null)
            return sessionAttribute.toString();
        return null;
    }

    public static void clearSession (HttpServletRequest request){
        request.getSession().invalidate();
    }
}
