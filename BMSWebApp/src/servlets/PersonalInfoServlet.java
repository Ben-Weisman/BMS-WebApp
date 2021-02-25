package servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
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

@WebServlet(name = "PersonalInfoServlet", urlPatterns = "/personalInfo")
public class PersonalInfoServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

/*      Update personal info: name, phone, email (username), password
        Requests are sent here only from the user who's his details are being changed.
        {
            "field To Update": "SOME_FIELD_NAME",
            "new value": "THE_NEW_VALUE",
        }*/


        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        BMSEngine engine = ServletUtils.getEngine(getServletContext());


        String streamData = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        streamData = builder.toString();
        JsonElement jsonElement = JsonParser.parseString(streamData);
        JsonObject json = jsonElement.getAsJsonObject();
        String newFieldValue = json.get(Constants.NEW_FIELD_VALUE_JSON_KEY_NAME).getAsString();
        String fieldToUpdate = json.get(Constants.FIELD_TO_UPDATE_JSON_KEY_NAME).getAsString();
        String usernameParam = SessionUtils.getMemberObject(req).getName();


        JsonObject respJSON = new JsonObject();
        String message;
        String status;

        try {
            detectAndUpdateField(fieldToUpdate,newFieldValue,usernameParam, engine);
            message = "Update finished successfully.";
            status = "ok";
        }  catch (InvalidInputException | NotfoundException | ExportToXmlException | JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            message = e.getMessage();
            status = "error";
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

    private void detectAndUpdateField(String fieldToUpdate, String newFieldValue, String usernameParam, BMSEngine engine) throws
            InvalidInputException, NotfoundException, ExportToXmlException, JAXBException{
        switch (fieldToUpdate){
            case Constants.MEMBER_EMAIL_FIELD_NAME:
                engine.updateMemberEmail(usernameParam,newFieldValue);
                break;
            case Constants.MEMBER_NAME_FIELD_NAME:
                engine.updateMemberName(usernameParam,newFieldValue);
                break;
            case Constants.MEMBER_PASSWORD_FIELD_NAME:
                engine.updateMemberPassword(usernameParam,newFieldValue);
                break;
            case Constants.MEMBER_PHONE_FIELD_NAME:
                engine.updateMemberPhone(usernameParam,newFieldValue);
                break;

        }
    }
}
