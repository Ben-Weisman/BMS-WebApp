package servlets.removals;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.member.Member;
import engine.engine.BMSEngine;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "RemoveRowersServlet", urlPatterns = "/removeRowers")
public class RemoveRowersServlet extends HttpServlet {


    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!SessionUtils.validateSession(req)) {
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }


        HttpSession session = req.getSession();
        Member sessionMember = (Member) session.getAttribute(Constants.MEMBER_OBJECT);
        if (!sessionMember.isAdmin()) {
            resp.sendError(403, "Access denied");
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

        int bookingID = jsonObject.get("bookingID").getAsInt();
        String rowers = jsonObject.get("rowers").getAsString();

        removeRowersFromBooking(bookingID,rowers);


        String status = "ok";
        String message = "Rowers removed successfully";
        JsonObject respJSON = new JsonObject();
        respJSON.addProperty("message", message);
        respJSON.addProperty("status", status);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(respJSON.toString());
        out.flush();

    }

    private void removeRowersFromBooking(int bookingID, String rowers) {
        BMSEngine engine = ServletUtils.getEngine(getServletContext());

        System.out.println("Server: Entered removeRowersFromBooking");
        rowers = rowers.replaceAll("\\s", "");
        String[] rowersArr = rowers.split(",");
        java.util.List<java.lang.Integer> ids = new ArrayList<>();
        for (int i = 0; i < rowersArr.length; i++) {
            ids.add(Integer.parseInt(rowersArr[i]));
        }
        engine.removeRowersFromBooking(bookingID,ids);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);

    }
}
