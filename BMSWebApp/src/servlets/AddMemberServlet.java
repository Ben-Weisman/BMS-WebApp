package servlets;

import com.google.gson.Gson;
import engine.classes.member.Level;
import engine.classes.member.MemberDetails;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

@WebServlet(name = "addMemberServlet", urlPatterns = "/addMember")
public class AddMemberServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("addMemberServlet: entered doPost");
        BufferedReader reader = req.getReader();
        String memberJsonString = reader.lines().collect(Collectors.joining());

        Member member = gson.fromJson(memberJsonString, Member.class);
        MemberDetails memberDetails = new MemberDetails(member.email, member.comments, member.memberName, member.password,
                member.phoneNumber, Integer.parseInt(member.memberAge), LocalDate.parse(member.joiningDate),
                LocalDate.parse(member.expirationDate), member.hasPrivateBoat, member.isAdmin,
                Level.valueOf(member.selectedLevel), member.privateBoatSerialNumber);

        try {
            ServletUtils.getEngine(getServletContext()).addMember(memberDetails);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(member.memberName + " was added successful");
        } catch (JAXBException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("ERROR");
        }
    }




    static class Member {
        String memberName;
        String memberAge;
        String comments;
        String selectedLevel;
        String joiningDate;
        String expirationDate;
        boolean hasPrivateBoat;
        String phoneNumber;
        String email;
        String password;
        boolean isAdmin;
        String privateBoatSerialNumber;
    }
}
