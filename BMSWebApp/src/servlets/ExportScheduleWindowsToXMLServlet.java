package servlets;

import engine.customExceptions.ExportToXmlException;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "ExportScheduleWindowsToXMLServlet", urlPatterns = "/exportScheduleWindows")
public class ExportScheduleWindowsToXMLServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("entered ExportScheduleWindowsServlet");
        resp.setContentType("application/xml");
        try(PrintWriter out = resp.getWriter()){
            out.write(ServletUtils.getEngine(getServletContext()).exportWindowsToXml());
        }
        catch (ExportToXmlException e) {
            resp.getWriter().write(e.getMessage());
        }
    }

}