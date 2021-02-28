package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.classes.boat.BoatType;
import engine.classes.member.Member;
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
import java.time.LocalDate;
import java.util.stream.Collectors;

@WebServlet(name = "updateMemberServlet", urlPatterns = "/updateMember")
public class UpdateMemberServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }


        BufferedReader reader = req.getReader();
        String updateMemberJsonString = reader.lines().collect(Collectors.joining());

        UpdateMemberDetails updateMemberDetails = new Gson().fromJson(updateMemberJsonString, UpdateMemberDetails.class);
        BMSEngine engine = ServletUtils.getEngine(getServletContext());
        try {
            Member member = engine.retrieveMemberPerID(Integer.parseInt(updateMemberDetails.memberID));
            engine.updateMemberName(member.getName(), updateMemberDetails.newName);
            engine.updateMemberAge(member.getName(), Integer.parseInt(updateMemberDetails.newAge));
            engine.updateMemberComments(member.getName(), updateMemberDetails.newComments);
            engine.updateMemberExpirationDate(member.getName(), LocalDate.parse(updateMemberDetails.newExpirationDate));
            engine.updateMemberHasPrivateBoat(member.getName(), updateMemberDetails.hasPrivateBoat);
            engine.updateMemberPrivateBoatSerialNumber(member.getName(), updateMemberDetails.privateBoatSerialNumber);
            engine.updateMemberPhone(member.getName(), updateMemberDetails.newPhone);
            engine.updateMemberEmail(member.getName(), updateMemberDetails.newEmail);
            engine.updateMemberPassword(member.getName(), updateMemberDetails.newPassword);
            engine.updateMemberIsManager(member.getName(), updateMemberDetails.isManager);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("ID: " + updateMemberDetails.memberID + " was updated successful");
        } catch (NotfoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (InvalidInputException | JAXBException | ExportToXmlException  e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String memberID = req.getParameter("memberID");
        boolean exist = ServletUtils.getEngine(getServletContext()).isMemberIDExists(Integer.parseInt(memberID));
        try (PrintWriter out = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(String.valueOf(exist));
        }
    }

    static class UpdateMemberDetails {
        String memberID;
        String newName;
        String newAge;
        String newComments;
        String newExpirationDate;
        boolean hasPrivateBoat;
        String privateBoatSerialNumber;
        String newPhone;
        String newEmail;
        String newPassword;
        boolean isManager;
    }
}