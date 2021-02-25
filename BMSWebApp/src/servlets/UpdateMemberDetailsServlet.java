package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import engine.classes.member.Member;
import engine.customExceptions.ExportToXmlException;
import engine.customExceptions.InvalidInputException;
import engine.customExceptions.NotfoundException;
import engine.engine.BMSEngine;
import sun.security.util.Length;
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

@WebServlet(name = "UpdateMemberDetailsServlet", urlPatterns = "/updateMemberData")
public class UpdateMemberDetailsServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

/*        The expected json structure is:
        {
            "field To Update": "SOME_FIELD_NAME",
                "new value": "THE_NEW_VALUE",
                "username to update": "THE_USERNAME"
        }*/


        String fieldToUpdate = req.getParameter(Constants.MEMBER_FIELD_TO_EDIT_PARAMETER_NAME);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());


        String data = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        data = builder.toString();

        JsonElement jsonElement = JsonParser.parseString(data);
        JsonObject json = jsonElement.getAsJsonObject();
        String newFieldValue = json.get("name").getAsString();

        //Not pulling username from session as we want to keep it flexible for other types of requests.
        // for example, an admin that changes someone else's details.
        String userNameParameter = req.getParameter(Constants.USERNAME);

        try {
            detectAndUpdateField(fieldToUpdate,newFieldValue,userNameParameter, engine);
        }  catch (InvalidInputException | NotfoundException | ExportToXmlException | JAXBException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

    private void detectAndUpdateField(String fieldToUpdate, String newFieldValue, String userNameParameter, BMSEngine engine) throws
            InvalidInputException, NotfoundException, ExportToXmlException, JAXBException{
        switch (fieldToUpdate){
            case Constants.MEMBER_EMAIL_FIELD_NAME:
                engine.updateMemberEmail(userNameParameter,newFieldValue);
                break;
            case Constants.MEMBER_NAME_FIELD_NAME:
                engine.updateMemberName(userNameParameter,newFieldValue);
                break;
            case Constants.MEMBER_PASSWORD_FIELD_NAME:
                engine.updateMemberPassword(userNameParameter,newFieldValue);
                break;
            case Constants.MEMBER_PHONE_FIELD_NAME:
                engine.updateMemberPhone(userNameParameter,newFieldValue);
                break;

        }
    }
}
