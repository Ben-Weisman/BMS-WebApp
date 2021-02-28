package servlets.getters;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.boat.Boat;
import engine.classes.windows.ScheduleWindow;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getAllScheduleWindowsServlet", urlPatterns = "/getAllScheduleWindows")
public class GetAllScheduleWindowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }


        List<ScheduleWindow> allScheduleWindows = ServletUtils.getEngine(getServletContext()).getReadOnlyScheduleWindowList();
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(allScheduleWindows));
        }
    }
}
