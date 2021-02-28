package servlets.updates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.booking.Booking;
import engine.classes.windows.ScheduleWindow;
import engine.customExceptions.ExportToXmlException;
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
import java.io.PrintWriter;
import java.nio.file.Path;

@WebServlet(name = "UpdateBookingWindow" , urlPatterns = "/updateWindow")
public class UpdateBookingWindow extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        * Json should look like:
        * newWindowID:
        * bookingID:
        * */


        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        StringBuilder builder = new StringBuilder();
        String line;
        String streamData;
        BufferedReader reader = req.getReader();

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int newWindowID = Integer.parseInt(jsonObject.get("newWindowID").getAsString());
        int bookingID = Integer.parseInt(jsonObject.get("bookingID").getAsString());

        ScheduleWindow newWindow = engine.retrieveScheduleWindowPerID(newWindowID);
        Booking theBooking = engine.retrieveBookingPerID(bookingID);

        String status = "ok";
        String message = "";
        JsonObject respJSON = new JsonObject();
        try {
            engine.editBookingWindow(newWindow,theBooking);
        } catch (ExportToXmlException | JAXBException e) {
            status = "error";
            message = "Failed to save the changes to the DB";
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        respJSON.addProperty("message",message);
        respJSON.addProperty("status",status);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(respJSON.toString());
        out.flush();

    }
}
