package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.customExceptions.InvalidBoatNameException;
import engine.customExceptions.InvalidTypeException;
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
import java.util.stream.Collectors;

@WebServlet(name="addBoatServlet", urlPatterns = "/addBoat")
public class AddBoatServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        System.out.println("addBoatServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String boatJsonString = reader.lines().collect(Collectors.joining());


        BoatDetails boatDetails = gson.fromJson(boatJsonString, BoatDetails.class);
        try {
            ServletUtils.getEngine(getServletContext()).addBoat(boatDetails.boatType, boatDetails.boatName, boatDetails.isPrivate);
            ServletUtils.getEngine(getServletContext()).getBoats().stream().map(boat -> boat.getBoatName()).forEach(System.out::println);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(boatDetails.boatName + " was added successful");
        } catch (InvalidBoatNameException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        } catch (InvalidTypeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("invalid Type");
        } catch (JAXBException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("JAXB error");
        }
    }




    static class BoatDetails {
        String boatName;
        String boatType;
        boolean isPrivate;
    }
}
