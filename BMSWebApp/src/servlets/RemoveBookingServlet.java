package servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.NotfoundException;
import engine.engine.BMSEngine;
import utils.ServletUtils;
import utils.SessionUtils;

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

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        StringBuilder builder = new StringBuilder();
        String streamData;
        String line;
        BufferedReader reader = req.getReader();

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String bookingID = jsonObject.get("bookingID").getAsString();


        try {
            ServletUtils.getEngine(getServletContext()).removeBookingRequest(bookingID);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + bookingID + " was removed successfully");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
