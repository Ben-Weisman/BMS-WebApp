package servlets.access;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminVerifierServlet", urlPatterns = "/adminVerify")
public class AdminVerifierServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // returns true / false if admin.

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        Member sessionMember = SessionUtils.getMemberObject(req);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isAdmin",sessionMember.isAdmin());
        String json = jsonObject.toString();
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        out.println(json);
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
