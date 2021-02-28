package servlets;

import constants.Constants;
import engine.customExceptions.ExportToXmlException;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "ExportMembersToXMLServlet", urlPatterns = "/exportMembers")
public class ExportMembersToXMLServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        System.out.println("entered ExportMembersServlet");
        resp.setContentType("application/xml");
        try(PrintWriter out = resp.getWriter()){
            out.write(ServletUtils.getEngine(getServletContext()).exportMembersToXml());
        }
        catch (ExportToXmlException | DatatypeConfigurationException e) {
            resp.getWriter().write(e.getMessage());
        }
    }
}
