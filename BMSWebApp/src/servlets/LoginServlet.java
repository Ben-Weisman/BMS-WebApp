package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.member.Member;
import engine.engine.BMSEngine;
import engine.users.ActiveUsersManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet (name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login servlet:entered doPost");
        String usernameFromSession = SessionUtils.getUsername(req);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        ActiveUsersManager activeUsersManager = ServletUtils.getActiveUserManager(getServletContext());


        // user already logged in
        if (usernameFromSession != null){
            if (engine.getMemberPerUsername(usernameFromSession).isAdmin())
                resp.sendRedirect(Constants.MAIN_MENU_URL_ADMIN);
            else resp.sendRedirect(Constants.MAIN_MENU_URL_REGULAR);
            return;
        }

        // User isn't logged in yet.
        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        String passwordFromParameter = req.getParameter(Constants.PASSWORD);
        if (usernameFromParameter == null || usernameFromParameter.trim().isEmpty() ||
                passwordFromParameter == null || passwordFromParameter.trim().isEmpty()){
            // No username is session and not in the parameter.
            // Redirect back to home page.
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
        }

        // Username exists in the parameter. Normalize it before proceeding.
        else {
            usernameFromParameter = usernameFromParameter.trim();
            passwordFromParameter = passwordFromParameter.trim();
        }

        // Check if username exists in DB .
        // we treat email address as the username.
        if (!engine.isMemberEmailAddressExistsInList(usernameFromParameter)) {
            // Username doesn't exists in DB.
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            System.out.println("User doesn't exists in the DB");
            return;
        }
        else if (engine.accessVerification(usernameFromParameter,passwordFromParameter) != null){
            // If got here - user isn't logged in, his username exists in the DB and the credentials he entered are correct.
            // In that case - add the username to the active users list.
            activeUsersManager.addUsername(usernameFromParameter);

            // Set the username in a session so it'll be available on each request.
            // Set as true to create a new session if one doesn't exists yet.
            req.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter.trim());

            if (engine.getMemberPerUsername(usernameFromParameter).isAdmin())
                resp.sendRedirect(Constants.MAIN_MENU_URL_ADMIN);
            else resp.sendRedirect(Constants.MAIN_MENU_URL_REGULAR);
        }
    }

}
