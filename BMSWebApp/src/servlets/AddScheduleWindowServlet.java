package servlets;

import com.google.gson.Gson;
import engine.classes.boat.BoatType;
import engine.classes.member.Level;
import engine.classes.member.MemberDetails;
import engine.classes.windows.ScheduleWindowDetails;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.InvalidInputException;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@WebServlet(name = "addScheduleWindowServlet", urlPatterns = "/addScheduleWindow")
public class AddScheduleWindowServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("addScheduleWindowServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String ScheduleWindowJsonString = reader.lines().collect(Collectors.joining());

        ScheduleWindow ScheduleWindow = gson.fromJson(ScheduleWindowJsonString, ScheduleWindow.class);
        ScheduleWindowDetails scheduleWindowDetails = new ScheduleWindowDetails(ScheduleWindow.ScheduleWindowName,
                LocalTime.parse(ScheduleWindow.startTime), LocalTime.parse(ScheduleWindow.endTime),
                ScheduleWindow.boatType.isEmpty() ? null : BoatType.valueOf(ScheduleWindow.boatType));
        try {
            ServletUtils.getEngine(getServletContext()).addNewWindow(scheduleWindowDetails);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(ScheduleWindow.ScheduleWindowName + " was added successful");
        } catch (ExportToXmlException | InvalidInputException | JAXBException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    static class ScheduleWindow {
        String ScheduleWindowName;
        String boatType;
        String startTime;
        String endTime;

    }
}
