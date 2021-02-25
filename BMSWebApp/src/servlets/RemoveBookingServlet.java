package servlets;

import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.NotfoundException;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "removeBookingServlet", urlPatterns = "/removeBooking")
public class RemoveBookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("removeBookingServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String bookingID = reader.lines().collect(Collectors.joining());

        try {
            ServletUtils.getEngine(getServletContext()).removeBookingRequest(bookingID);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + bookingID + " was removed successful");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
