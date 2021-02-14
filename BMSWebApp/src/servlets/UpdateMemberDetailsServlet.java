package servlets;

import com.google.gson.Gson;
import constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UpdateMemberDetailsServlet", urlPatterns = "/updateMemberData")
public class UpdateMemberDetailsServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String fieldToUpdate = req.getParameter(Constants.MEMBER_FIELD_TO_EDIT_PARAMETER_NAME);

        String data = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        data = builder.toString();



    }
}
