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


@WebServlet(name = "GetAllPendingRequestsServlet", urlPatterns = "/getPending")
public class GetAllPendingRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        List<Booking> pendingRequests = engine.getPendingBookingRequests();
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()){
            String json = new Gson().toJson(pendingRequests);
            out.println(json);
            out.flush();
        }



    }
}
