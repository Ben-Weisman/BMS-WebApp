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


@WebServlet(name = "GetBookingsServlet", urlPatterns = "/GetBookings")
public class GetBookingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        Integer userID = SessionUtils.getMemberID(req);
        StringBuilder builder = new StringBuilder();
        String line;
        String streamData;
        BufferedReader reader = req.getReader();
        String listStyleParam;
        List<Booking> resBookingList;
        String json;

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }

        streamData = builder.toString();
        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        listStyleParam = jsonObject.get(Constants.LIST_STYLE_PARAM_JSON).toString();
        resBookingList = getListByParam(listStyleParam,engine,userID);
        json = new Gson().toJson(resBookingList);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();
    }

    private List<Booking> getListByParam(String listStyleParam, BMSEngine engine, Integer userID) {
         switch (listStyleParam.toLowerCase()) {
             case "pending" : return engine.getPendingBookingRequests();
            case "all" : return engine.getBookingList();
            case "future" : return engine.generateAllFutureBookingRequests(userID);
            default : return null;
        }
    }
}
