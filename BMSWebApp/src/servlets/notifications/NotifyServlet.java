package servlets.notifications;

import com.google.gson.Gson;
import constants.Constants;
import engine.alerts.MemberNotification;
import engine.alerts.MemberNotificationManager;
import engine.classes.member.Member;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "NotifyServlet", urlPatterns = "/pullNotifications")
public class NotifyServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!SessionUtils.validateSession(req)){
            resp.sendRedirect(Constants.LOGIN_PAGE_URL);
            return;
        }

        Member sessionMember = SessionUtils.getMemberObject(req);
        MemberNotificationManager memberNotificationManager = sessionMember.getNotificationsManager();
        int version = memberNotificationManager.getVersion();
        List<MemberNotification> adminNotifications = sessionMember.getAdminNotifications(version);
        List<MemberNotification> autoNotifications = sessionMember.getAutoNotification();

        /*
            Messages are sent in the following json template:
            {
                adminNotifications:[ Notifications_Array ],
                adminNotificationsVersion: the version (int),
                autoNotifications: [ Notifications_Array ]
            }
         */
        MessagesAndVersion responseObj = new MessagesAndVersion(adminNotifications,version, autoNotifications);
        String jsonResponse = new Gson().toJson(responseObj);
        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()){
            out.println(jsonResponse);
            out.flush();
        }

        memberNotificationManager.clearAutoMessages();

     }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private static class MessagesAndVersion {
        final private List<MemberNotification> adminNotifications;
        final private int adminNotificationsVersion;
        final private List<MemberNotification> autoNotifications;


        public MessagesAndVersion(List<MemberNotification> notifications, int version, List<MemberNotification> autoNotifications){
            this.adminNotifications = notifications;
            this.adminNotificationsVersion = version;
            this.autoNotifications = autoNotifications;
        }
    }
}
