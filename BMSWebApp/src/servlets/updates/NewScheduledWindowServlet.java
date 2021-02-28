package servlets.updates;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.boat.BoatType;
import engine.classes.windows.ScheduleWindow;
import engine.classes.windows.ScheduleWindowDetails;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.InvalidInputException;
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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "NewScheduledWindowServlet", urlPatterns = "/newScheduleWindow")
public class NewScheduledWindowServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        /*
            json should look like:
                name:
                startTime:
                EndTime:
                boatType:

         */

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        ScheduleWindowDetails scheduleWindowDetails;
        String streamData = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();
        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (isValidJson(jsonObject)){
            scheduleWindowDetails = initScheduleWindowDetails(jsonObject, engine);
            try {
                ScheduleWindow sw = engine.addNewWindow(scheduleWindowDetails);
                triggerResponse(resp, sw);
            } catch (InvalidInputException e) {
                e.printStackTrace();
                resp.sendError(403,"Could not create new booking due to data corruption");
            } catch (JAXBException | ExportToXmlException e) {
                System.out.println(e.getMessage());
                resp.sendError(403,"Failed to save the new record to file.");
            }
        }



    }

    private void triggerResponse(HttpServletResponse resp, ScheduleWindow sw) throws IOException {
        Map<String, Integer> windowIdRes = new HashMap<>();
        windowIdRes.put("windowID", sw.getID());
        String jsonResponse = new Gson().toJson(windowIdRes);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonResponse);
        out.flush();

    }

    private boolean isValidJson(JsonObject jsonObject) {
        if (jsonObject.get("name") == null || jsonObject.get("name").equals(""))
            return false;
        else if (jsonObject.get("startTime") == null || jsonObject.get("endTime") == null)
            return false;
        else return true;
    }

    private ScheduleWindowDetails initScheduleWindowDetails(JsonObject jsonObject, BMSEngine engine) {
        String name = jsonObject.get("name").getAsString();
        LocalTime startTime = LocalTime.parse(jsonObject.get("startTime").getAsString());
        LocalTime endTime = LocalTime.parse(jsonObject.get("endTime").getAsString());
        BoatType boatType = null;
        if (jsonObject.get("boatType") != null)
            boatType = engine.getBoatTypeFromString(jsonObject.get("boatType").getAsString());
        return new ScheduleWindowDetails(name,startTime,endTime,boatType);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
}
