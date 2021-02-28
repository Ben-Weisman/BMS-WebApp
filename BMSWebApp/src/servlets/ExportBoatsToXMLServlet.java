package servlets;

import constants.Constants;
import engine.customExceptions.ExportToXmlException;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "ExportBoatsToXMLServlet", urlPatterns = "/exportBoats")
public class ExportBoatsToXMLServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }



        System.out.println("entered ExportBoatsServlet");
        resp.setContentType("application/xml");
        try(PrintWriter out = resp.getWriter()){
            out.write(ServletUtils.getEngine(getServletContext()).exportBoatsToXml());
        }
        catch (ExportToXmlException e) {
            resp.getWriter().write(e.getMessage());
        }
    }
}
