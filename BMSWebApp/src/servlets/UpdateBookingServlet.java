package servlets;

import com.google.gson.Gson;
import engine.classes.boat.BoatType;
import engine.customExceptions.BoatAssignmentException;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.InvalidInputException;
import engine.customExceptions.NotfoundException;
import engine.engine.BMSEngine;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "updateBookingServlet" , urlPatterns = "/updateBooking")
public class UpdateBookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        String updateBookingJsonString = reader.lines().collect(Collectors.joining());

        UpdateBookingDetails updateBookingDetails = new Gson().fromJson(updateBookingJsonString, UpdateBookingDetails.class);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        try {
            engine.updateBookingPracticeDate(updateBookingDetails.bookingID, LocalDate.parse(updateBookingDetails.practiceDate));
            engine.updateBookingRequestedWindowID(updateBookingDetails.bookingID, updateBookingDetails.newWindowID);
            engine.updateBookingAssignedBoatID(Integer.parseInt(updateBookingDetails.bookingID),
                    Integer.parseInt(updateBookingDetails.newAssignedBoatID));

            engine.updateBookingRequestedBoatTypes(updateBookingDetails.bookingID,
                    stringArrayToBoatTypeList(updateBookingDetails.newBoatTypes));
            engine.updateBookingOtherParticipatingRowersID(updateBookingDetails.bookingID,
                    stringArrayToIntegerList(updateBookingDetails.otherParticipatingRowersIDs));

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + updateBookingDetails.bookingID + " was updated successful");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (InvalidInputException | JAXBException | BoatAssignmentException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String bookingID = req.getParameter("bookingID");
        boolean exist = ServletUtils.getEngine(getServletContext()).retrieveBookingPerID(Integer.parseInt(bookingID)) != null;
        try (PrintWriter out = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(String.valueOf(exist));
        }
    }

    private List<BoatType> stringArrayToBoatTypeList(String[] arr) {
        List<BoatType> toReturn = new ArrayList<>();
        for (String type : arr) {
            toReturn.add(BoatType.valueOf(type));
        }
        return toReturn;
    }

    private List<Integer> stringArrayToIntegerList(String[] arr){
        List<Integer> toReturn = new ArrayList<>();
        for (String str : arr){
            toReturn.add(Integer.valueOf(str));
        }
        return toReturn;
    }

    static class UpdateBookingDetails {
        String bookingID;
        String practiceDate;
        String newWindowID;
        String newAssignedBoatID;
        String[] newBoatTypes;
        String[] otherParticipatingRowersIDs;

    }
}
