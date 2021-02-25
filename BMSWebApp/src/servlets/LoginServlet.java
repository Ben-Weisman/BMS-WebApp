package servlets;

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

        Integer userID = SessionUtils.getMemberID(req);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        ActiveUsersManager activeUsersManager = ServletUtils.getActiveUserManager(getServletContext());



        if (userID != null){
            // user already logged in
            Member m;
            if ((m = engine.retrieveMemberPerID(userID)) != null){
                if (m.isAdmin())
                    resp.sendRedirect(Constants.MAIN_MENU_URL_ADMIN);
                else resp.sendRedirect(Constants.MAIN_MENU_URL_REGULAR);
            }
            else return;

        }

        // User isn't logged in yet.
        String emailFromParameter = req.getParameter(Constants.EMAIL);
        String passwordFromParameter = req.getParameter(Constants.PASSWORD);
        if (emailFromParameter == null || emailFromParameter.trim().isEmpty() ||
                passwordFromParameter == null || passwordFromParameter.trim().isEmpty()){
            // No username & password in the request param.
            // Redirect back to login page.
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        // Username & password exists in the parameter. Normalize it before proceeding.
        else {
            emailFromParameter = emailFromParameter.trim();
            passwordFromParameter = passwordFromParameter.trim();
        }

        // Check if username exists in DB .
        Member member;
        if ((member = engine.accessVerification(emailFromParameter,passwordFromParameter)) == null) {
            // Username doesn't exists in DB.
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            System.out.println("User doesn't exists in the DB");
        }

        else {
            // user logged in successfully. Add to active users list
            activeUsersManager.addUserID(member.getID());

            // Set session attributes.
            req.getSession(true).setAttribute(Constants.MEMBER_ID, member.getID());
            req.getSession(true).setAttribute(Constants.MEMBER_OBJECT, member);
            if (member.isAdmin())
                resp.sendRedirect(Constants.MAIN_MENU_URL_ADMIN);
            else resp.sendRedirect(Constants.MAIN_MENU_URL_REGULAR);
        }
    }

}
