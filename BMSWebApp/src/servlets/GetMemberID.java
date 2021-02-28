package servlets;

import com.google.gson.JsonObject;
import constants.Constants;
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

@WebServlet(name = "GetMemberID", urlPatterns = "/memberID")
public class GetMemberID extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        Integer memberID = SessionUtils.getMemberID(req);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberID", memberID);
        String json = jsonObject.toString();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();
    }
}
