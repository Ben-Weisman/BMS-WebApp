package servlets.updates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.member.Member;
import engine.customExceptions.BoatAssignmentException;
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
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "UpdateBookingAssignedBoatID", urlPatterns = "/assignBoat")
public class UpdateBookingAssignedBoatID extends HttpServlet {



    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        HttpSession session = req.getSession();
        Member sessionMember = (Member) session.getAttribute(Constants.MEMBER_OBJECT);
        if (!sessionMember.isAdmin()){
            resp.sendError(403,"Access denied");
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        StringBuilder builder = new StringBuilder();
        String streamData;
        String line;
        BufferedReader reader = req.getReader();

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int bookingIDToUpdate = jsonObject.get("bookingID").getAsInt();
        int assignedBoatID = jsonObject.get("boatID").getAsInt();

        JsonObject respJSON = new JsonObject();
        String message;
        String status = "error";



        try {
            engine.updateBookingAssignedBoatID(bookingIDToUpdate,assignedBoatID);
            message = "Update finished successfully.";
            status = "ok";
        } catch (BoatAssignmentException | InvalidInputException | NotfoundException | JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            message = e.getMessage();
        }

        respJSON.addProperty("message",message);
        respJSON.addProperty("status",status);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(respJSON.toString());
        out.flush();


    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }




    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

}
