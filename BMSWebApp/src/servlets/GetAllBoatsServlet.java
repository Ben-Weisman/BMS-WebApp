package servlets;

import com.google.gson.Gson;
import engine.classes.boat.Boat;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebFault;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getAllBoatsServlet", urlPatterns = "/getAllBoats")
public class GetAllBoatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Boat> allBoats = ServletUtils.getEngine(getServletContext()).getReadOnlyBoatsListList();
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(allBoats));
        }
    }
}
