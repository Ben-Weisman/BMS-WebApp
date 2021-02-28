package servlets.xml;

import engine.customExceptions.*;
import engine.engine.BMSEngine;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@WebServlet(name = "importListingFromXMLServlet", urlPatterns = "/importListing")
@MultipartConfig
public class ImportListingFromXMLServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        try {
            if (req.getPart("boatsFile") != null) {
                engine.importBoatsFromXML(true, readFromInputStream(req.getPart("boatsFile").getInputStream()));
                writeSuccessfulMessage(req, resp, "boatsFile");
            }
            if (req.getPart("membersFile") != null) {
                engine.importMembersFromXML(true, readFromInputStream(req.getPart("membersFile").getInputStream()));
                writeSuccessfulMessage(req, resp, "membersFile");

            }
            if (req.getPart("scheduleWindowsFile") != null) {
                engine.importWindowsFromXML(true, readFromInputStream(req.getPart("scheduleWindowsFile").getInputStream()));
                writeSuccessfulMessage(req, resp, "scheduleWindowsFile");
            }
        } catch (ImportXmlException | InvalidBoatNameException | InvalidTypeException | InvalidInputException | IllegalDataInXmlFileException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    private void writeSuccessfulMessage(HttpServletRequest req, HttpServletResponse resp, String fileType) throws IOException, ServletException {
        resp.getWriter().write(req.getPart(fileType).getName() + " was imported successful");
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
