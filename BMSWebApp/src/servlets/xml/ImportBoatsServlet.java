package servlets.xml;

import constants.Constants;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name="importBoatsServlet", urlPatterns = "/importBoats")
public class ImportBoatsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }


        System.out.println("entered import boats!!");
        Collection<Part> parts = req.getParts();
        for (Part part : parts) {
            System.out.println(part.getInputStream().toString());
        }
    }
}
