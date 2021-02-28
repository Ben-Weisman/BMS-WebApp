package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "GetFutureBookingReqServlet", urlPatterns = "/futureBookings")
public class GetFutureBookingReqServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
// requests for bookings are possible in two fashions:
        // 1. Get future bookings
        // 2. Get pending bookings

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        Integer userID = SessionUtils.getMemberID(req);
        List<Booking> resList = engine.generateAllFutureBookingRequests(userID);

        String resJson = new Gson().toJson(resList);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        out.println(resJson);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private List<Booking> getListByParam(String listStyleParam, BMSEngine engine, Integer userID) {
         switch (listStyleParam.toLowerCase()) {
             case "pending" : return engine.getPendingBookingRequests();
            case "future" : return engine.generateAllFutureBookingRequests(userID);
            default : return null;
        }
    }
}
