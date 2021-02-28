package servlets;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "GetMembersServlet", urlPatterns = "/members")
public class GetMembersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        List<Member> members = new ArrayList<>();
        for (Member m : engine.getMembers()){
            if (m.getID() != SessionUtils.getMemberObject(req).getID())
                members.add(m);
        }
        String json = new Gson().toJson(members);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();

    }
}
