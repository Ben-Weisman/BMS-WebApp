package servlets;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.boat.BoatType;
import engine.classes.booking.Booking;
import engine.classes.booking.BookingDetails;
import engine.customExceptions.InvalidInputException;
import engine.engine.BMSEngine;
import jdk.nashorn.internal.parser.JSONParser;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "NewBookingRequestServlet", urlPatterns = "/newBookingRequest")
public class NewBookingRequestServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        /*
            Json should look like:
            {
                'requestedWindowID': int,
                'memberOrderedID': int,
                'requestedBoatTypes': [Array of String],
                'otherParticipatingRowersIDs':[Array of int],
                'requestedPracticeDate': String formatted yyyy-mm-dd (??)
            }
         */


        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());

        String streamData;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        BookingDetails newBookingDetails;
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (validateJsonFields(jsonObject)) {
            try {
                newBookingDetails = initBookingRequestDetails(jsonObject, engine);
                engine.addNewBookingRequest(newBookingDetails);
            }catch (DateTimeParseException | InvalidInputException | JAXBException e){
                System.out.println(e.getMessage());
                resp.sendError(403,"Could not create new booking due to data corruption");
            }
        }



    }

    private BookingDetails initBookingRequestDetails(JsonObject jsonObject, BMSEngine engine) throws DateTimeParseException {


        int memberOrderedID = jsonObject.get("memberOrderedID").getAsInt();

        String[] boatTypesAsStringArray = new Gson().fromJson(jsonObject.get("requestedBoatTypes").toString(), String[].class);
        List<BoatType> requestedBoatTypes = engine.getBoatTypeListFromStringArray(boatTypesAsStringArray);

        int[] otherRowersIDsDataFromJson = new Gson().fromJson(jsonObject.get("otherParticipatingRowersIDs").toString(),int[].class);
        List<Integer> otherParticipatingRowersIDs = Arrays.stream(otherRowersIDsDataFromJson).boxed().collect(Collectors.toList());

        int requestedWindowID = jsonObject.get("requestedWindowID").getAsInt();

        String requestedPracticeDateFromJson = jsonObject.get("requestedPracticeDate").getAsString();
        LocalDate requestedPracticeDate = LocalDate.parse(requestedPracticeDateFromJson);
        return  new BookingDetails(memberOrderedID, requestedBoatTypes,otherParticipatingRowersIDs,requestedWindowID, requestedPracticeDate);
    }

    private boolean validateJsonFields(JsonObject jsonObject) {
        if (jsonObject.get("requestedWindowID") == null || jsonObject.get("requestedWindowID").getAsInt() == 0)
            return false;
        else if (jsonObject.get("memberOrderedID") == null || jsonObject.get("memberOrderedID").getAsInt() == 0)
            return false;
        else if (jsonObject.get("requestedBoatTypes") == null)
            return false;
        else if (jsonObject.get("otherParticipatingRowersIDs") == null)
            return false;
        else if (jsonObject.get("requestedPracticeDate") == null)
            return false;
        else return true;
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