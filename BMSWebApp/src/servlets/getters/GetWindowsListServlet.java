package servlets.getters;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.windows.ScheduleWindow;
import engine.engine.BMSEngine;
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


@WebServlet(name = "GetWindowsList", urlPatterns = "/getWindowsList")
public class GetWindowsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    /*
        response json format:
         scheduleWindowID:
         name:
         startTime:
         endTime:
         boatType: // optional
     */

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        List<ScheduleWindow> windows = engine.getScheduleWindows();
        String json = new Gson().toJson(windows);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();

    }
}
