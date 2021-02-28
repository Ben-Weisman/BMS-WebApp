package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.deploy.util.SessionProperties;
import constants.Constants;
import engine.classes.boat.Boat;
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


@WebServlet(name = "GetAvailableBoatsServlet", urlPatterns = "/getAvailableBoats")
public class GetAvailableBoatsServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }
        BMSEngine engine = ServletUtils.getEngine(getServletContext());

        String line;
        StringBuilder builder = new StringBuilder();
        String streamData;
        BufferedReader reader = req.getReader();

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int bookingID = jsonObject.get("bookingID").getAsInt();
        List<Boat> resBoatList = engine.getAvailableBoats(bookingID);

        // processing response
        String responseJson = new Gson().toJson(resBoatList);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(responseJson);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        processRequest(req,resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
