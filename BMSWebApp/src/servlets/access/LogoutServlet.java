package servlets.access;

import constants.Constants;
import engine.users.ActiveUsersManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {


    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ActiveUsersManager activeUsersManager = ServletUtils.getActiveUserManager(getServletContext());
        Integer userIDFromSession = SessionUtils.getMemberID(req);
        if (userIDFromSession != null){
            activeUsersManager.removeUsername(userIDFromSession);
            SessionUtils.clearSession(req);
        }
        resp.sendRedirect(Constants.LOGIN_PAGE_URL);
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
}
