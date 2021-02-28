package servlets.updates;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.boat.BoatType;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.InvalidInputException;
import engine.customExceptions.NotfoundException;
import engine.engine.BMSEngine;
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
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "updateBoatServlet", urlPatterns = "/updateBoat")
public class UpdateBoatServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        System.out.println("updateBoatServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String updateBoatJsonString = reader.lines().collect(Collectors.joining());

        UpdateBoatDetails updateBoatDetails = gson.fromJson(updateBoatJsonString, UpdateBoatDetails.class);
            BMSEngine engine = ServletUtils.getEngine(getServletContext());
        try {
            if(!updateBoatDetails.newName.isEmpty()) engine.updateBoatName(updateBoatDetails.boatID, updateBoatDetails.newName);
            if(!updateBoatDetails.selectedBoatType.isEmpty()) engine.updateBoatType(updateBoatDetails.boatID, BoatType.valueOf(updateBoatDetails.selectedBoatType));
            engine.updateBoatIsPrivate(updateBoatDetails.boatID, updateBoatDetails.isPrivate);
            engine.updateBoatIsWide(updateBoatDetails.boatID, updateBoatDetails.isWide);
            engine.updateBoatHasCoxswain(updateBoatDetails.boatID, updateBoatDetails.hasCoxwain);
            engine.updateBoatIsCoastal(updateBoatDetails.boatID, updateBoatDetails.isCoastal);
            engine.updateBoatIsFunctioning(updateBoatDetails.boatID, updateBoatDetails.isFunctioning);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + updateBoatDetails.boatID + " was updated successful");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (InvalidInputException| JAXBException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String boatID = req.getParameter("boatID");
        boolean exist = ServletUtils.getEngine(getServletContext()).isPrivateBoatIDExistsInBoatsList(Integer.parseInt(boatID));
        try (PrintWriter out = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(String.valueOf(exist));
        }
        System.out.println("updateBoatServlet: end doGet()");
    }


    static class UpdateBoatDetails {
        String boatID;
        String newName;
        boolean isPrivate;
        boolean isWide;
        boolean hasCoxwain;
        boolean isCoastal;
        boolean isFunctioning;
        String selectedBoatType;
    }
}

