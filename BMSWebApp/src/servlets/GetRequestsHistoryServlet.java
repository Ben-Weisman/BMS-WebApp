package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.booking.Booking;
import engine.engine.BMSEngine;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "GetRequestsHistoryServlet", urlPatterns = "/getReqHistory")
public class GetRequestsHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;

        }

        Integer userID = SessionUtils.getMemberID(req);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        List<Booking> reqHistory = engine.generateRequestsHistoryOfTheUSer(userID);
        String json = new Gson().toJson(reqHistory);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();
    }
}
