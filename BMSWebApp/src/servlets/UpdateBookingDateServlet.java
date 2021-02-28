package servlets;

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
import java.time.LocalDate;


@WebServlet(name = "UpdateBookingDateServlet" , urlPatterns = "/updateBookingDate")
public class UpdateBookingDateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         * Json should look like:
         * bookingID:
         * newDate:
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

        String jsonDate = jsonObject.get("newDate").getAsString();
        int bookingID = Integer.parseInt(jsonObject.get("bookingID").getAsString());
        Booking bookingToChange = engine.retrieveBookingPerID(bookingID);

        bookingToChange.setRequestedPracticeDate(LocalDate.parse(jsonDate));

        

    }

}
