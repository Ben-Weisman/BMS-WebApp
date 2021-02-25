package utils;

import constants.Constants;
import engine.classes.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static Integer getMemberID(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null){
            Integer userIDFromSession = (Integer) session.getAttribute(Constants.MEMBER_ID);
            if (userIDFromSession != null)
                return userIDFromSession;
            else return null;
            }
        else return null;

    }

    public static Member getMemberObject(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(Constants.MEMBER_OBJECT);
        return member;
    }

    public static boolean validateSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session == null)
            return false;
        else{
            Object sessionAttribute = session.getAttribute(Constants.MEMBER_ID);
            return sessionAttribute != null;
        }

    }

    public static void clearSession (HttpServletRequest request){
        request.getSession().invalidate();
    }

}
