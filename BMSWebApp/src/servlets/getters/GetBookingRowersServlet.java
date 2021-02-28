package servlets.getters;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.booking.Booking;
import engine.classes.member.Member;
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


@WebServlet(name = "GetBookingRowersServlet", urlPatterns = "/getRowers")
public class GetBookingRowersServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!SessionUtils.validateSession(req)) {
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        StringBuilder builder = new StringBuilder();
        String streamData;
        String line;
        BufferedReader reader = req.getReader();

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        streamData = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        BMSEngine engine = ServletUtils.getEngine(getServletContext());

        int bookingID = jsonObject.get("bookingID").getAsInt();
        Booking booking = engine.retrieveBookingPerID(bookingID);
        List<Integer> rowersIDs = booking.getOtherParticipatingRowersID();
        List<Member> rowers = engine.retrieveMembersAsListPerID(rowersIDs);

        String json = new Gson().toJson(rowers);
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);

    }
}
