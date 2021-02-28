package servlets.removals;

import com.google.gson.Gson;
import constants.Constants;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.NotfoundException;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "removeMemberServlet", urlPatterns = "/removeMember")
public class RemoveMemberServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }


        System.out.println("removeMemberServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String memberID = reader.lines().collect(Collectors.joining());

        try {
            ServletUtils.getEngine(getServletContext()).removeMember(memberID);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + memberID + " was removed successful");
        } catch (NotfoundException | ExportToXmlException | JAXBException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
