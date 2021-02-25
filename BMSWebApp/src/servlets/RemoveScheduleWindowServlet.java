package servlets;

import engine.customExceptions.NotfoundException;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "removeScheduleWindowServlet", urlPatterns = "/removeScheduleWindow")
public class RemoveScheduleWindowServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("removeScheduleWindowServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String scheduleWindowName = reader.lines().collect(Collectors.joining());

        try {
            ServletUtils.getEngine(getServletContext()).removeScheduleWindow(scheduleWindowName);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(scheduleWindowName + " was removed successful");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
