package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.boat.Boat;
import engine.classes.booking.Booking;
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

@WebServlet(name="getAllBookingsServlet", urlPatterns = "/getAllBookings")
public class GetAllBookingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        List<Booking> allBookings = ServletUtils.getEngine(getServletContext()).getReadOnlyBookingRequestsList();
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(allBookings));
            out.flush();
        }
    }
}
