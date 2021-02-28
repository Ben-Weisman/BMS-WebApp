package servlets.updates;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.boat.BoatType;
import engine.customExceptions.BoatAssignmentException;
import engine.customExceptions.InvalidInputException;
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
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "updateScheduleWindowServlet", urlPatterns = "/updateScheduleWindow")
public class UpdateScheduleWindowServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }


        BufferedReader reader = req.getReader();
        String updateScheduleWindowJsonString = reader.lines().collect(Collectors.joining());

        UpdateScheduleWindowDetails updateScheduleWindowDetails = new Gson().fromJson(updateScheduleWindowJsonString,UpdateScheduleWindowDetails.class);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        try {
            if (!updateScheduleWindowDetails.newStartTime.isEmpty())
                engine.updateScheduleWindowStartTime(updateScheduleWindowDetails.windowName,
                    LocalTime.parse(updateScheduleWindowDetails.newStartTime));
            if (!updateScheduleWindowDetails.newEndTime.isEmpty())
                engine.updateScheduleWindowEndTime(updateScheduleWindowDetails.windowName,
                    LocalTime.parse(updateScheduleWindowDetails.newEndTime));
            if (!updateScheduleWindowDetails.newBoatType.isEmpty())
                engine.updateScheduleWindowBoatType(updateScheduleWindowDetails.windowName,
                    BoatType.valueOf(updateScheduleWindowDetails.newBoatType));
            if (!updateScheduleWindowDetails.newScheduleWindowName.isEmpty())
                engine.updateScheduleWindowName(updateScheduleWindowDetails.windowName,
                    updateScheduleWindowDetails.newScheduleWindowName);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(updateScheduleWindowDetails.windowName + " was updated successful");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (InvalidInputException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String windowName = req.getParameter("windowName");
        boolean exist = ServletUtils.getEngine(getServletContext()).isActivityNameExistsInList(windowName);
        try (PrintWriter out = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(String.valueOf(exist));
        }
    }


    static class UpdateScheduleWindowDetails {
        String windowName;
        String newScheduleWindowName;
        String newStartTime;
        String newEndTime;
        String newBoatType;
    }
}
