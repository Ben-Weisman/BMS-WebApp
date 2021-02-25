package servlets;

import com.google.gson.Gson;
import engine.customExceptions.*;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "removeBoatServlet", urlPatterns = "/removeBoat")
public class RemoveBoatServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("removeBoatServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String removeBoatJsonString = reader.lines().collect(Collectors.joining());

       RemoveBoatDetails removeBoatDetails = gson.fromJson(removeBoatJsonString, RemoveBoatDetails.class);
        try {
            ServletUtils.getEngine(getServletContext()).removeBoat(removeBoatDetails.boatID, removeBoatDetails.forceRemove);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + removeBoatDetails.boatID + " was removed successful");
        } catch (NotfoundException | ExportToXmlException | JAXBException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        } catch (NeedToChangeOtherListingException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }


    static class RemoveBoatDetails {
        String boatID;
        boolean forceRemove;
    }
}
