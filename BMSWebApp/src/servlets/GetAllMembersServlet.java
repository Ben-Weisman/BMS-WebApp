package servlets;

import com.google.gson.Gson;
import engine.classes.boat.Boat;
import engine.classes.member.Member;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getAllMembersServlet", urlPatterns = "/getAllMembers")
public class GetAllMembersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Member> allMembers = ServletUtils.getEngine(getServletContext()).getReadOnlyMembersList();
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(allMembers));
        }
    }
}
