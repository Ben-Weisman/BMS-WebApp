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
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetMembersWithoutReqRowersServlet", urlPatterns = "/relevant")
public class GetMembersWithoutReqRowersServlet extends HttpServlet {


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

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
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

        int bookingIDToUpdate = jsonObject.get("bookingID").getAsInt();

        List<Member> relevantMembers = engine.getMembersListWithoutUser(bookingIDToUpdate);

        String json = new Gson().toJson(relevantMembers);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}


